package com.example.nelo.domain.usecases

import com.example.nelo.domain.repositories.HistoryRepository

class ClearHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke() {
        historyRepository.clearHistory()
    }
}