package com.example.nelo.domain.usecases

import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.model.FilterModel
import com.example.nelo.domain.repositories.MangaRepository

class GetFilteredMangasUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke(title: String, filter: FilterModel, page: Int): Result<FeedResponseModel> {
        return repository.getFilteredMangas(title = title, filter = filter, page = page)
    }
}