package com.narayanagroup.assiginment.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.narayanagroup.assiginment.models.items

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiplePlayers(list: List<items>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertsinglePlayers(list: items)

    @Query("SELECT * FROM repos_table")
    fun getPlayers(): PagingSource<Int, items>

    @Query("DELETE FROM repos_table")
    suspend fun clearRepos()

    @Query("SELECT COUNT(id) from repos_table")
    suspend fun count(): Int

}