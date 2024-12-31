package com.example.nelo.domain.usecases

import com.example.nelo.data.model.HistoryEntity
import com.example.nelo.domain.repositories.HistoryRepository

class AddHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyEntity: HistoryEntity) {
        historyRepository.addHistory(historyEntity = historyEntity)
    }
}