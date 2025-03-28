package com.example.nelo.domain.usecases

import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.repositories.MangaRepository

class GetChapterDetailsUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke(chapterUrl: String, chapterTitle: String): Result<ChapterModel> {
        return repository.getChapterDetails(chapterUrl = chapterUrl, chapterTitle = chapterTitle)
    }
}