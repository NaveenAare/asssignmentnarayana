package com.narayanagroup.assiginment.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.narayanagroup.assiginment.models.owner
import java.lang.reflect.Type

class TeamConverter {

    private val gSon = Gson()

    private val type: Type = object : TypeToken<owner?>() {}.type

    @TypeConverter
    fun from(owner: owner?): String? {
        if (owner == null) {
            return null
        }

        return gSon.toJson(owner, type)
    }

    @TypeConverter
    fun to(teamString: String?): owner? {
        if (teamString == null) {
            return null
        }
        return gSon.fromJson(teamString, type)
    }
}