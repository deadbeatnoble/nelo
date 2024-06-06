package com.example.nelo.data.library

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLibrary(libraryEntity: LibraryEntity)

    @Query("SELECT * FROM library ORDER BY id ASC")
    fun getAllLibrary(): LiveData<List<LibraryEntity>>

    @Query("DELETE FROM library WHERE mangaUrl = :mangaUrl")
    suspend fun deleteLibrary(mangaUrl: String)

    @Query("SELECT EXISTS (SELECT * FROM library WHERE mangaUrl = :mangaUrl)")
    suspend fun existsLibrary(mangaUrl: String): Boolean

}