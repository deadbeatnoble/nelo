package com.example.nelo.domain.usecases

import com.example.nelo.data.model.LibraryEntity
import com.example.nelo.domain.repositories.LibraryRepository

class AddLibraryUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(libraryEntity: LibraryEntity) {
        return libraryRepository.addLibrary(libraryEntity = libraryEntity)
    }
}