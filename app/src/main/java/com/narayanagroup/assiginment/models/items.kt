package com.narayanagroup.assiginment.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.narayanagroup.assiginment.utils.TeamConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(TeamConverter::class)
@Entity(tableName = "repos_table")
data class items(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val full_name: String,
    val html_url:String,
    val description:String,
    val owner: owner,
    val updated_at:String
) : Parcelable