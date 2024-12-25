package com.example.nelo.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.usecases.GetChapterDetailsUseCase
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
class SharedViewModel @Inject constructor(
    private val getMangaDetailsUseCase: GetMangaDetailsUseCase,
    private val getChapterDetailsUseCase: GetChapterDetailsUseCase
):ViewModel() {

    private val _mangaDetailsUiState = MutableStateFlow<UiState<MangaModel>>(UiState.Loading)
    val mangaDetailsUiState: StateFlow<UiState<MangaModel>> = _mangaDetailsUiState

    private val _chapterDetailsUiState = MutableStateFlow<UiState<ChapterModel>>(UiState.Loading)
    val chapterDetailsUiState: StateFlow<UiState<ChapterModel>> = _chapterDetailsUiState

    private val _prevChapterDetails = MutableStateFlow(ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList()))
    val prevChapterDetails: StateFlow<ChapterModel> = _prevChapterDetails

    private val _nextChapterDetails = MutableStateFlow(ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList()))
    val nextChapterDetails: StateFlow<ChapterModel> = _nextChapterDetails

    //methods

    fun getMangaDetails(mangaUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = getMangaDetailsUseCase(mangaUrl = mangaUrl)

            if (result.isSuccess) {
                Log.e("TEST", "MangaUiState is Successful, chapter lists -> " + UiState.Success(result.getOrNull()!!).data.chapterList.toString())

                _mangaDetailsUiState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.e("TEST", "MangaUiState is Failed")
                _mangaDetailsUiState.value = UiState.Error(result.exceptionOrNull()?.message ?: "An error occurred")
            }
        }
    }

    fun getChapterDetails(chapterUrl: String, chapterTitle: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (mangaDetailsUiState.value is UiState.Success<MangaModel>){
                val result = getChapterDetailsUseCase.invoke(
                    chapterUrl = chapterUrl,
                    chapterTitle = chapterTitle
                )

                if (result.isSuccess) {
                    Log.e("TEST", "ChapterUiState is Successful -> " + UiState.Success(result.getOrNull()!!).data.toString())

                    _chapterDetailsUiState.value = UiState.Success(result.getOrNull()!!)

                    _prevChapterDetails.value = getPreviousChapterDetails()
                    Log.e("TEST","Prev Chapter is Successful -> " + _prevChapterDetails.value.toString())
                    _nextChapterDetails.value = getNextChapterDetails()
                    Log.e("TEST","Next Chapter is Successful -> " + _nextChapterDetails.value.toString())
                } else {
                    _chapterDetailsUiState.value =
                        UiState.Error(result.exceptionOrNull()?.message ?: "An error occurred")
                }
            }
        }
    }

    private fun getPreviousChapterDetails(): ChapterModel {
        val index = (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList.indexOfFirst {
            it.chapterUrl == (chapterDetailsUiState.value as UiState.Success<ChapterModel>).data.chapterUrl
        }
        Log.e("INDEX", "Current chapter -> $index")
        Log.e("Manga UIState", mangaDetailsUiState.value.toString())
        return if (index < (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList.size - 1) (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList[index + 1] else ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
    }

    private fun getNextChapterDetails(): ChapterModel {
        val index = (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList.indexOfFirst {
            it.chapterUrl == (chapterDetailsUiState.value as UiState.Success<ChapterModel>).data.chapterUrl
        }
        Log.e("INDEX", "Current chapter -> $index")
        Log.e("Manga UIState", mangaDetailsUiState.value.toString())
        return if (index > 0) (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList[index - 1] else ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
    }
}