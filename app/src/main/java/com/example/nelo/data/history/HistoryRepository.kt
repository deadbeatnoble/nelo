package com.example.nelo.data.history

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(
    private val historyDao: HistoryDao
) {
    val getAllHistory: LiveData<List<HistoryEntity>> = historyDao.getAllHistory()

    suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity = historyEntity)
    }

    suspend fun deleteHistory(mangaChapterUrl: String) {
        historyDao.deleteHistory(mangaChapterUrl = mangaChapterUrl)
    }

    suspend fun clearHistory() {
        historyDao.clearHistory()
    }

    suspend fun existHistory(mangaChapterUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            historyDao.existHistory(mangaChapterUrl = mangaChapterUrl)
        }
    }

}