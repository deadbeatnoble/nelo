package com.example.nelo.data.local.library

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nelo.data.model.LibraryEntity

@Database(entities = [LibraryEntity::class], version = 1, exportSchema = false)
abstract class LibraryDatabase: RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}