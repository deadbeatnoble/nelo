package com.example.nelo.domain.usecases

import androidx.lifecycle.LiveData
import com.example.nelo.data.model.LibraryEntity
import com.example.nelo.domain.repositories.LibraryRepository

class GetAllLibraryUseCase(
    private val libraryRepository: LibraryRepository
) {
    operator fun invoke(): LiveData<List<LibraryEntity>> {
        return libraryRepository.getAllLibrary()
    }
}