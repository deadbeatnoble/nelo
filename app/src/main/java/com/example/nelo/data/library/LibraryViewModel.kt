package com.example.nelo.data.library

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LibraryViewModel(application: Application): AndroidViewModel(application) {
    private val getAllLibrary: LiveData<List<LibraryEntity>>
    private val repository: LibraryRepository

    init {
        val libraryDao = LibraryDatabase.getDatabase(application).libraryDao()

        repository = LibraryRepository(libraryDao)
        getAllLibrary = repository.getAllLibrary
    }

    private var _existsLibraryState = mutableStateOf(value = false)
    val existsLibraryState = _existsLibraryState
    fun existState(mangaUrl: String) {
        _existsLibraryState.value = existLibrary(mangaUrl = mangaUrl)
    }

    fun existLibrary(mangaUrl: String): Boolean = runBlocking {
        withContext(Dispatchers.IO) {
            repository.existLibrary(mangaUrl = mangaUrl)
        }
    }

    fun onLibraryClicked(libraryEntity: LibraryEntity) {
        if (existLibrary(mangaUrl = libraryEntity.mangaUrl)) {
            deleteLibrary(mangaUrl = libraryEntity.mangaUrl)
            _existsLibraryState.value = false
        } else {
            addLibrary(libraryEntity = libraryEntity)
            _existsLibraryState.value = true
        }
    }

    fun addLibrary(libraryEntity: LibraryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLibrary(libraryEntity = libraryEntity)
        }
    }

    fun deleteLibrary(mangaUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLibrary(mangaUrl = mangaUrl)
        }
    }

    private var _mangaLibrary = MutableStateFlow(value = mutableListOf<LibraryEntity>())
    val mangaLibrary = _mangaLibrary.asStateFlow()

    fun getLibrary() {
        getAllLibrary.observeForever { library ->
            library?.let {
                _mangaLibrary.value = it.map {
                    LibraryEntity(
                        id = it.id,
                        mangaTitle = it.mangaTitle,
                        mangaThumbnail = it.mangaThumbnail,
                        mangaUrl = it.mangaUrl
                    )
                }.toMutableList()
            }
        }
    }

}