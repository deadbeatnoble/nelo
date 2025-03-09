package com.example.nelo.data.repositories

import com.example.nelo.data.remote.scraper.Parser
import com.example.nelo.data.remote.scraper.WebScraper
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.model.FilterModel
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
                val dataResult = parser.chapterDetailsParser(doc = document, chapterTitle = chapterTitle, referrer = "https://www.nelomanga.com/")
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
        //https://www.nelomanga.com/manga-list/hot-manga?page=2
        //old web -> "https://manganato.com/genre-all${if (page > 1) "/${page}" else ""}?type=topview"
        val documentResult = pageScraper.scrapeWebPage("https://www.nelomanga.com/manga-list/hot-manga${if (page > 1) "?page=${page}" else ""}")
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
        //https://www.nelomanga.com/manga-list/latest-manga
        //old web -> "https://manganato.com/genre-all${if (page > 1) "/${page}" else ""}"
        val documentResult = pageScraper.scrapeWebPage("https://www.nelomanga.com/manga-list/latest-manga${if (page > 1) "?page=${page}" else ""}")
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

    override suspend fun getNewestMangas(page: Int): Result<FeedResponseModel> {
        //https://www.nelomanga.com/manga-list/new-manga
        //old web -> "https://manganato.com/genre-all${if (page > 1) "/${page}" else ""}?type=newest"
        val documentResult = pageScraper.scrapeWebPage("https://www.nelomanga.com/manga-list/new-manga${if (page > 1) "?page=${page}" else ""}")
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

    override suspend fun getFilteredMangas(title: String, filter: FilterModel, page: Int): Result<FeedResponseModel> {
        //https://www.nelomanga.com/search/story/solo_leveling
        val url = buildString {
            append("https://www.nelomanga.com/search/story/${title.replace(" ", "_")}${if (page > 1) "?page=${page}" else ""}")
        }
        /*val url = buildString {
            append("https://m.manganelo.com/advanced_search?s=all/")

            if (filter.include.isNotEmpty()) {
                append("&g_i=_${filter.include.joinToString("_")}_")
            }

            if (filter.exclude.isNotEmpty()) {
                append("&g_e=_${filter.include.joinToString("_")}_")
            }

            if (filter.status.isNotEmpty()) {
                append("&sts=${filter.status}")
            }

            if (filter.orderBy.isNotEmpty()) {
                append("&orby=${filter.orderBy}")
            }

            if (page > 1) {
                append("&page=${page}")
            }

            if (filter.keyType.isNotEmpty()) {
                append("&keyt=${filter.keyType}")
            }

            if (filter.keyWord.isNotEmpty()) {
                append("&keyw=${filter.keyWord}")
            }
        }*/

        val documentData = pageScraper.scrapeWebPage(url)
        return documentData.fold(
            onSuccess = { document ->
                val dataResult = parser.searchFeedParser(document)
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

}