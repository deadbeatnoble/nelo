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

    var mangaUrl = ""

    //methods

    fun getMangaDetails(mangaUrl: String) {
        _mangaDetailsUiState.value = UiState.Loading
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
        _chapterDetailsUiState.value = UiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("TEST", "ChapterUiState is Launching")

            val result = getChapterDetailsUseCase.invoke(
                chapterUrl = chapterUrl,
                chapterTitle = chapterTitle
            )

            if (result.isSuccess) {
                Log.e("TEST", "ChapterUiState is Successful -> " + UiState.Success(result.getOrNull()!!).data.toString())
                _chapterDetailsUiState.value = UiState.Success(result.getOrNull()!!)

                if (mangaDetailsUiState.value !is UiState.Success) {
                    getMangaDetails(mangaUrl)
                    mangaUrl = ""
                }

                mangaDetailsUiState.collect { state ->
                    if (state is UiState.Success) {
                        _prevChapterDetails.value = getPreviousChapterDetails()
                        _nextChapterDetails.value = getNextChapterDetails()

                        Log.e("TEST","Prev Chapter is Successful -> " + _prevChapterDetails.value.toString())
                        Log.e("TEST","Next Chapter is Successful -> " + _nextChapterDetails.value.toString())

                        return@collect
                    }
                }
            } else {
                Log.e("TEST", "ChapterUiState is Successful -> " + UiState.Error(result.exceptionOrNull()?.message.toString()).message)
                _chapterDetailsUiState.value =
                    UiState.Error(result.exceptionOrNull()?.message ?: "An error occurred")
            }
        }
    }

    private fun getPreviousChapterDetails(): ChapterModel {
        val chapterState = chapterDetailsUiState.value
        val mangaState = mangaDetailsUiState.value

        return if (mangaState is UiState.Success && chapterState is UiState.Success) {
            val index = mangaState.data.chapterList.indexOfFirst {
                it.chapterUrl == chapterState.data.chapterUrl
            }
            Log.e("INDEX", "Current chapter -> $index")
            Log.e("Manga UIState", mangaDetailsUiState.value.toString())
            if (index < (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList.size - 1) (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList[index + 1] else ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
        } else {
            ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
        }

    }

    private fun getNextChapterDetails(): ChapterModel {
        val chapterState = chapterDetailsUiState.value
        val mangaState = mangaDetailsUiState.value

        return if (mangaState is UiState.Success && chapterState is UiState.Success) {
            val index = (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList.indexOfFirst {
                it.chapterUrl == (chapterDetailsUiState.value as UiState.Success<ChapterModel>).data.chapterUrl
            }
            Log.e("INDEX", "Current chapter -> $index")
            Log.e("Manga UIState", mangaDetailsUiState.value.toString())
            if (index > 0) (mangaDetailsUiState.value as UiState.Success<MangaModel>).data.chapterList[index - 1] else ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
        } else {
            ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
        }
    }
}