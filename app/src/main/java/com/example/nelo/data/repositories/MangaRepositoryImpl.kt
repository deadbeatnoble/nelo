package com.example.nelo.data.repositories

import com.example.nelo.data.remote.scraper.Parser
import com.example.nelo.data.remote.scraper.WebScraper
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.repositories.MangaRepository

class MangaRepositoryImpl(
    private val pageScraper: WebScraper,
    private val parser: Parser
): MangaRepository {
    override suspend fun getMangaDetails(mangaUrl: String): Result<MangaModel> {
        val documentResult = pageScraper.scrapeWebPage(mangaUrl)
        return documentResult.fold(
            onSuccess = { document ->
                val dataResult = parser.mangaDetailsParser(doc = document, mangaUrl = mangaUrl)
                dataResult.fold(
                    onSuccess = { data ->
                        Result.success(data)
                    },
                    onFailure = { throwable ->
                        Result.failure(throwable)
                    }
                )
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    override suspend fun getChapterDetails(chapterUrl: String): ChapterModel {
        TODO("Not yet implemented")
    }

    override suspend fun getPopularMangas(): FeedResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestMangas(): FeedResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun getNewestMangas(): FeedResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun getFilteredMangas(): FeedResponseModel {
        TODO("Not yet implemented")
    }

}