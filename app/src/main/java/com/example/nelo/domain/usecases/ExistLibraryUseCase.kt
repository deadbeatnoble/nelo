package com.example.nelo.domain.usecases

import com.example.nelo.domain.repositories.LibraryRepository

class ExistLibraryUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(mangaUrl: String): Boolean {
        return libraryRepository.existLibrary(mangaUrl = mangaUrl)
    }
}