package com.example.nelo.data.repositories

import androidx.lifecycle.LiveData
import com.example.nelo.data.local.history.HistoryDao
import com.example.nelo.data.model.HistoryEntity
import com.example.nelo.domain.repositories.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
): HistoryRepository {
    //val getAllHistory: LiveData<List<HistoryEntity>> = historyDao.getAllHistory()
    override fun getAllHistory(): LiveData<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }

    override suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity = historyEntity)
    }

    override suspend fun deleteHistory(mangaChapterUrl: String) {
        historyDao.deleteHistory(mangaChapterUrl = mangaChapterUrl)
    }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }

    override suspend fun existHistory(mangaChapterUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            historyDao.existHistory(mangaChapterUrl = mangaChapterUrl)
        }
    }

}