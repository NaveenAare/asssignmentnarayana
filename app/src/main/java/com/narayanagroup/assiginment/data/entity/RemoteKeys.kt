package com.narayanagroup.assiginment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val isEndReached: Boolean
)