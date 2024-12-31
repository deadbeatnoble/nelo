package com.example.nelo.domain.usecases

import com.example.nelo.domain.repositories.HistoryRepository

class ExistHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(mangaChapterUrl: String): Boolean {
        return historyRepository.existHistory(mangaChapterUrl = mangaChapterUrl)
    }
}