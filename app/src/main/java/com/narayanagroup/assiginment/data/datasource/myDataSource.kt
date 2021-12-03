package com.narayanagroup.assiginment.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.narayanagroup.assiginment.api.githubApi
import com.narayanagroup.assiginment.models.items
import com.narayanagroup.assiginment.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


class myDataSource(private val githubApi: githubApi, private val s:String) :
    PagingSource<Int, items>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, items> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = githubApi.getPlayers("search/repositories?q="+s+":readme&type=repository",10, page)
            val players = response.playersList
            LoadResult.Page(
                data = players,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey =  page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, items>): Int? {
        return state.anchorPosition
    }
}