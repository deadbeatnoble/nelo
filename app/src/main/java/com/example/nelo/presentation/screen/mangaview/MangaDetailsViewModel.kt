package com.example.nelo.presentation.screen.mangaview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.usecases.GetMangaDetailsUseCase
import com.example.nelo.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailsViewModel @Inject constructor(
    private val mangaDetailsUseCase: GetMangaDetailsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<MangaModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<MangaModel>> = _uiState

    fun getMangaDetails(mangaUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = mangaDetailsUseCase(mangaUrl = mangaUrl)

            if (result.isSuccess) {
                _uiState.value = UiState.Success(result.getOrNull()!!)
            } else {
                _uiState.value = UiState.Error(result.exceptionOrNull()?.message ?: "An error occurred")
            }
        }
    }
}