package com.narayanagroup.assiginment.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.narayanagroup.assiginment.api.githubApi
import com.narayanagroup.assiginment.data.datasource.myDataSource
import com.narayanagroup.assiginment.data.db.AppDataBase
import com.narayanagroup.assiginment.data.remotediators.MyRemoteMediator
import com.narayanagroup.assiginment.models.items
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyRepository @Inject constructor(
    private val githubApi: githubApi,
    private val db: AppDataBase
) {


    private val pagingSourceFactory = { db.playersDao.getPlayers() }

    /**
     * for caching
     */
    @ExperimentalPagingApi
    suspend fun getPlayers(s: String, net:Boolean): Flow<PagingData<items>> {
        if(s!="") {
            db.playersDao.clearRepos()
        }

            return Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = false
                ),
                remoteMediator = MyRemoteMediator(
                    githubApi,
                    db, s
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow //can also return livedata



    }
    /**
     * Use this if you dont want to cache data to room
     */
    /*return Pager(
    config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
    pagingSourceFactory = {
        PlayersDataSource(playersApi,s)
    }
    ).flow }*/

    /**
     * The same thing but with Livedata
     */
    fun getPlayersLiveData(s: String): LiveData<PagingData<items>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = {
                myDataSource(githubApi,s)
            }
        ).liveData

    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

}