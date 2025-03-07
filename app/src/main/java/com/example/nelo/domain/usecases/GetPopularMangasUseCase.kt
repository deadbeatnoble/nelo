package com.example.nelo.domain.usecases

import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.repositories.MangaRepository

class GetPopularMangasUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke(page: Int): Result<FeedResponseModel> {
        return repository.getPopularMangas(page = page)
    }
}