package com.example.nelo.domain.repositories

import androidx.lifecycle.LiveData
import com.example.nelo.data.model.HistoryEntity

interface HistoryRepository {
    fun getAllHistory(): LiveData<List<HistoryEntity>>
    suspend fun addHistory(historyEntity: HistoryEntity)
    suspend fun deleteHistory(mangaChapterUrl: String)
    suspend fun clearHistory()
    suspend fun existHistory(mangaChapterUrl: String): Boolean
}