package com.example.nelo.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nelo.data.model.LibraryEntity
import com.example.nelo.domain.usecases.AddLibraryUseCase
import com.example.nelo.domain.usecases.DeleteLibraryUseCase
import com.example.nelo.domain.usecases.ExistLibraryUseCase
import com.example.nelo.domain.usecases.GetAllLibraryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getAllLibraryUseCase: GetAllLibraryUseCase,
    private val addLibraryUseCase: AddLibraryUseCase,
    private val deleteLibraryUseCase: DeleteLibraryUseCase,
    private val existLibraryUseCase: ExistLibraryUseCase
): ViewModel() {
    private val getAllLibrary: LiveData<List<LibraryEntity>> = getAllLibraryUseCase()
    //private val repository: LibraryRepositoryImpl

    /*init {
        //val libraryDao = LibraryDatabase.getDatabase(application).libraryDao()

        //repository = LibraryRepositoryImpl(libraryDao)
    }*/

    private var _existsLibraryState = mutableStateOf(value = false)
    val existsLibraryState = _existsLibraryState
    fun existState(mangaUrl: String) {
        _existsLibraryState.value = existLibrary(mangaUrl = mangaUrl)
    }

    private fun existLibrary(mangaUrl: String): Boolean = runBlocking {
        withContext(Dispatchers.IO) {
            existLibraryUseCase(mangaUrl = mangaUrl)
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

    private fun addLibrary(libraryEntity: LibraryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            addLibraryUseCase(libraryEntity = libraryEntity)
        }
    }

    private fun deleteLibrary(mangaUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteLibraryUseCase(mangaUrl = mangaUrl)
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