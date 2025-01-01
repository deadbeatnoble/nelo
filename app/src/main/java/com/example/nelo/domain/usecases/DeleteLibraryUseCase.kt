package com.example.nelo.domain.usecases

import com.example.nelo.domain.repositories.LibraryRepository

class DeleteLibraryUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(mangaUrl: String) {
        return libraryRepository.deleteLibrary(mangaUrl = mangaUrl)
    }
}