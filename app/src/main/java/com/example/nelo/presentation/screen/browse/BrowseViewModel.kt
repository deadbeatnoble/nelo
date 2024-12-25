package com.example.nelo.presentation.screen.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.usecases.GetPopularMangasUseCase
import com.example.nelo.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getPopularMangasUseCase: GetPopularMangasUseCase
): ViewModel() {
    private val _feedUiState = MutableStateFlow<UiState<List<MangaModel>>>(UiState.Loading)
    val feedUiState: StateFlow<UiState<List<MangaModel>>> = _feedUiState

    private var currentPage: Int = 1
    private var currentCategory: String = "Popular"


    init {
        getPopularMangas()
    }

    fun getPopularMangas(reset: Boolean = false) {
        if (reset) {
            currentPage = 1
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = getPopularMangasUseCase(page = currentPage)

            if (result.isSuccess) {
                _feedUiState.value = when (_feedUiState.value) {
                    is UiState.Success -> UiState.Success((_feedUiState.value as UiState.Success<List<MangaModel>>).data + (result.getOrNull()?.data ?: emptyList()))
                    else -> UiState.Success(result.getOrNull()?.data ?: emptyList())
                }
                currentPage++

                    Log.e("POPULAR MANGAS", "size: " + (_feedUiState.value as UiState.Success<List<MangaModel>>).data.size.toString() + " -> " + (_feedUiState.value as UiState.Success<List<MangaModel>>).data.lastOrNull()?.title)
                //_feedUiState.value = UiState.Success(result.getOrNull()!!.data)
            } else {
                _feedUiState.value = UiState.Error(result.exceptionOrNull()?.message ?: "An error occurred")
            }
        }
    }

    fun switchCategory(category: String) {
        if (category == currentCategory) {
            return
        }
        currentCategory = category
        when(category) {
            "popular" -> getPopularMangas(reset = true)
            "latest" -> {}
            "newest" -> {}
        }
    }

}