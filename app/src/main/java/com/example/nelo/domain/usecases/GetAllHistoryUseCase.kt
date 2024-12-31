package com.example.nelo.domain.usecases

import androidx.lifecycle.LiveData
import com.example.nelo.data.model.HistoryEntity
import com.example.nelo.domain.repositories.HistoryRepository

class GetAllHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): LiveData<List<HistoryEntity>> {
        return historyRepository.getAllHistory()
    }
}