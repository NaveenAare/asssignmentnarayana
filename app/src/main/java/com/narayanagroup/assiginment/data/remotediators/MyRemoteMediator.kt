 package com.narayanagroup.assiginment.data.remotediators

import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.narayanagroup.assiginment.api.githubApi
import com.narayanagroup.assiginment.data.entity.RemoteKeys
import com.narayanagroup.assiginment.data.db.AppDataBase
import com.narayanagroup.assiginment.models.items
import com.narayanagroup.assiginment.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class MyRemoteMediator(
    private val service: githubApi,
    private val db: AppDataBase,
    private val s:String
) : RemoteMediator<Int, items>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, items>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.playersDao.count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {

                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getPlayers("search/repositories?q="+s+":readme&type=repository",10, page)

            var playersList = apiResponse.playersList
            var endOfPaginationReached=false
           if (page==1000000000){
               endOfPaginationReached = false

           }else{
               endOfPaginationReached = false
           }


            db.withTransaction {

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                var pl =playersList.sortedBy { it.id }

                db.playersDao.insertMultiplePlayers(pl)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }


}