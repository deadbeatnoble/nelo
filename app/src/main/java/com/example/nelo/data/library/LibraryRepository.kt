package com.example.nelo.data.library

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LibraryRepository(
    private val libraryDao: LibraryDao
) {
    val getAllLibrary: LiveData<List<LibraryEntity>> = libraryDao.getAllLibrary()

    suspend fun addLibrary(libraryEntity: LibraryEntity) {
        libraryDao.addLibrary(libraryEntity = libraryEntity)
    }

    suspend fun deleteLibrary(mangaUrl: String) {
        libraryDao.deleteLibrary(mangaUrl = mangaUrl)
    }

    suspend fun existLibrary(mangaUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            libraryDao.existsLibrary(mangaUrl = mangaUrl)
        }
    }

}