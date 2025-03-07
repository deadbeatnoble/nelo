package com.example.nelo.data.local.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nelo.data.model.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}