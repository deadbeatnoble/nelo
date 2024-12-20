package com.example.nelo.domain.usecases

import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.repositories.MangaRepository

class GetMangaDetailsUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke(mangaUrl: String) : Result<MangaModel> {
        return repository.getMangaDetails(mangaUrl = mangaUrl)
    }
}