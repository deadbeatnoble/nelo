package com.example.nelo.domain.usecases

import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.repositories.MangaRepository

class GetLatestMangasUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke(): FeedResponseModel {
        return repository.getLatestMangas()
    }
}