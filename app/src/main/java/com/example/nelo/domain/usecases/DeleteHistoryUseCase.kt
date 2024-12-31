package com.example.nelo.domain.usecases

import com.example.nelo.domain.repositories.HistoryRepository

class DeleteHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(mangaChapterUrl: String) {
        historyRepository.deleteHistory(mangaChapterUrl = mangaChapterUrl)
    }
}