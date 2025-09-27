package com.example.wbmonitor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class MonitorDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: MonitorDatabase? = null

        fun getInstance(context: Context): MonitorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MonitorDatabase::class.java,
                    "wb_monitor_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
