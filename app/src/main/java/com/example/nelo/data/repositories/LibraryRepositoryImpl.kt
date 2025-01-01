package com.example.nelo.data.repositories

import androidx.lifecycle.LiveData
import com.example.nelo.data.local.library.LibraryDao
import com.example.nelo.data.model.LibraryEntity
import com.example.nelo.domain.repositories.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LibraryRepositoryImpl(
    private val libraryDao: LibraryDao
): LibraryRepository {
    //val getAllLibrary: LiveData<List<LibraryEntity>> = libraryDao.getAllLibrary()
    override fun getAllLibrary(): LiveData<List<LibraryEntity>> {
        return libraryDao.getAllLibrary()
    }

    override suspend fun addLibrary(libraryEntity: LibraryEntity) {
        libraryDao.addLibrary(libraryEntity = libraryEntity)
    }

    override suspend fun deleteLibrary(mangaUrl: String) {
        libraryDao.deleteLibrary(mangaUrl = mangaUrl)
    }

    override suspend fun existLibrary(mangaUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            libraryDao.existsLibrary(mangaUrl = mangaUrl)
        }
    }

}