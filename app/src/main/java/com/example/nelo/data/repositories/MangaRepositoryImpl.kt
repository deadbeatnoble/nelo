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

    override suspend fun getChapterDetails(chapterUrl: String, chapterTitle: String): Result<ChapterModel> {
        val documentResult = pageScraper.scrapeWebPage(chapterUrl)
        return documentResult.fold(
            onSuccess = { document ->
                val dataResult = parser.chapterDetailsParser(doc = document, chapterTitle = chapterTitle, referrer = chapterUrl)
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

    override suspend fun getPopularMangas(page: Int): Result<FeedResponseModel> {
        val documentResult = pageScraper.scrapeWebPage("https://manganato.com/genre-all${if (page > 1) "/${page}" else ""}?type=topview")
        return documentResult.fold(
            onSuccess = { document ->
                val dataResult = parser.feedParser(document)
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

    override suspend fun getLatestMangas(page: Int): Result<FeedResponseModel> {
        val documentResult = pageScraper.scrapeWebPage("https://manganato.com/genre-all${if (page > 1) "/${page}" else ""}")
        return documentResult.fold(
            onSuccess = { document ->
                val dataResult = parser.feedParser(document)
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

    override suspend fun getNewestMangas(): FeedResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun getFilteredMangas(): FeedResponseModel {
        TODO("Not yet implemented")
    }

}