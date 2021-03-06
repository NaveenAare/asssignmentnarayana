package com.narayanagroup.assiginment.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.narayanagroup.assiginment.data.dao.PlayersDao
import com.narayanagroup.assiginment.data.entity.RemoteKeys
import com.narayanagroup.assiginment.data.dao.RemoteKeysDao
import com.narayanagroup.assiginment.models.items




/**
 * Build on top of [SQLiteDatabase], see [Room](https://developer.android.com/training/data-storage/room)
 */

//Entities that are used
@Database(
    entities = [items::class, RemoteKeys::class],
    version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract val playersDao: PlayersDao
    abstract val remoteKeysDao: RemoteKeysDao

    //Room should only be initiated once, marked volatile to be thread safe.
    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(
                    context
                ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "app_db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}