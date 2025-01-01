package com.example.nelo.domain.repositories

import androidx.lifecycle.LiveData
import com.example.nelo.data.model.LibraryEntity

interface LibraryRepository {
    suspend fun addLibrary(libraryEntity: LibraryEntity)
    fun getAllLibrary(): LiveData<List<LibraryEntity>>
    suspend fun deleteLibrary(mangaUrl: String)
    suspend fun existLibrary(mangaUrl: String): Boolean
}