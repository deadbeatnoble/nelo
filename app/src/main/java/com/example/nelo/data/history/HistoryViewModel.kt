package com.example.nelo.data.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HistoryViewModel(application: Application): AndroidViewModel(application) {

    private val getAllHistory: LiveData<List<HistoryEntity>>
    private val repository: HistoryRepository

    init {
        val historyDao = HistoryDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(historyDao)
        getAllHistory = repository.getAllHistory
    }

    fun addHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!existHistory(mangaChapterUrl = historyEntity.mangaChapterUrl)) {
                repository.addHistory(historyEntity = historyEntity)
            }
        }
    }

    fun deleteHistory(mangaChapterUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistory(mangaChapterUrl = mangaChapterUrl)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearHistory()
        }
    }

    fun existHistory(mangaChapterUrl: String): Boolean = runBlocking {
        withContext(Dispatchers.IO) {
            repository.existHistory(mangaChapterUrl = mangaChapterUrl)
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