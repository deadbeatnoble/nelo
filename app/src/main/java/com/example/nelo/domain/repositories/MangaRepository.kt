package com.example.nelo.domain.repositories

import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.model.FilterModel
import com.example.nelo.domain.model.MangaModel

interface MangaRepository {
    suspend fun getMangaDetails(mangaUrl: String): Result<MangaModel>
    suspend fun getChapterDetails(chapterUrl: String, chapterTitle: String): Result<ChapterModel>
    suspend fun getPopularMangas(page: Int): Result<FeedResponseModel>
    suspend fun getLatestMangas(page: Int): Result<FeedResponseModel>
    suspend fun getNewestMangas(page: Int): Result<FeedResponseModel>
    suspend fun getFilteredMangas(title: String = "", filter: FilterModel, page: Int): Result<FeedResponseModel>
}