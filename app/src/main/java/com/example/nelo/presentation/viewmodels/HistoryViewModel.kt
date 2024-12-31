package com.example.nelo.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nelo.data.model.HistoryEntity
import com.example.nelo.domain.usecases.AddHistoryUseCase
import com.example.nelo.domain.usecases.ClearHistoryUseCase
import com.example.nelo.domain.usecases.DeleteHistoryUseCase
import com.example.nelo.domain.usecases.ExistHistoryUseCase
import com.example.nelo.domain.usecases.GetAllHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllHistoryUseCase: GetAllHistoryUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase,
    private val existHistoryUseCase: ExistHistoryUseCase
): ViewModel() {

    private val getAllHistory: LiveData<List<HistoryEntity>> = getAllHistoryUseCase()
    //private val repository: HistoryRepositoryImpl

    /*init {
        //val historyDao = HistoryDatabase.getDatabase(application).historyDao()
        //repository = HistoryRepositoryImpl(historyDao)
    }*/

    fun addHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!existHistoryUseCase(mangaChapterUrl = historyEntity.mangaChapterUrl)) {
                addHistoryUseCase(historyEntity = historyEntity)
            }
        }
    }

    fun deleteHistory(mangaChapterUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHistoryUseCase(mangaChapterUrl = mangaChapterUrl)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            clearHistoryUseCase()
        }
    }

    fun existHistory(mangaChapterUrl: String): Boolean = runBlocking {
        withContext(Dispatchers.IO) {
            existHistoryUseCase(mangaChapterUrl = mangaChapterUrl)
        }
    }

    private var _mangaHistory = MutableStateFlow(value = mutableListOf<HistoryEntity>())
    val mangaHistory = _mangaHistory.asStateFlow()

    fun getHistory() {
        getAllHistory.observeForever { history ->
            history?.let {
                _mangaHistory.value = it.map {
                    HistoryEntity(
                        id = it.id,
                        mangaTitle = it.mangaTitle,
                        mangaThumbnail = it.mangaThumbnail,
                        mangaUrl = it.mangaUrl,
                        mangaChapter = it.mangaChapter,
                        mangaChapterUrl = it.mangaChapterUrl,
                    )
                }.toMutableList()
            }
        }
    }


}