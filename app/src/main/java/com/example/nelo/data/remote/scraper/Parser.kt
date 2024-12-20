package com.example.nelo.data.remote.scraper

import com.example.nelo.domain.model.AuthorModel
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.GenreModel
import com.example.nelo.domain.model.MangaModel
import org.jsoup.nodes.Document

class Parser {
    fun mangaDetailsParser(doc: Document, mangaUrl: String): Result<MangaModel> {
        val authors = mutableListOf<AuthorModel>()
        var status: String? = null
        val genres = mutableListOf<GenreModel>()

        var updatedAt: String? = null
        var view: String? = null

        val chapterList = mutableListOf<ChapterModel>()

        doc.getElementsByClass("variations-tableInfo").firstOrNull()?.select("tbody")?.firstOrNull()?.let { tableRows ->
            tableRows.children().forEach { tableRow ->
                when(tableRow.getElementsByClass("table-label").text().replace("\"", "").split(" ")[0]) {
                    "Author(s)" -> tableRow.getElementsByClass("table-value").select("a").forEach { author ->
                        authors.add(
                            AuthorModel(
                                name = author.text(),
                                url = author.attr("href") ?: null
                            )
                        )
                    }
                    "Status" -> status = tableRow.getElementsByClass("table-value").text() ?: null
                    "Genres" -> tableRow.getElementsByClass("table-value").select("a").forEach { genre ->
                        genres.add(
                            GenreModel(
                                name = genre.text(),
                                id = genre.attr("href").split("-")[1],
                                url = genre.attr("href") ?: null
                            )
                        )
                    }
                }
            }
        }

        doc.getElementsByClass("story-info-right-extent").firstOrNull()?.let { pList ->
            pList.children().forEach { p ->
                when(p.getElementsByClass("stre-label").text().replace("\"", "").split(" ")[0]) {
                    "Updated" -> updatedAt = p.getElementsByClass("stre-value").text() ?: null
                    "View" -> view = p.getElementsByClass("stre-value").text() ?: null
                }
            }
        }

        doc.getElementsByClass("panel-story-chapter-list").firstOrNull()?.let {
            it.select("ul.row-content-chapter").firstOrNull()?.let { list ->
                list.children().forEach { chapter ->
                    chapterList.add(
                        ChapterModel(
                            title = chapter.select("a").text() ?: null,
                            view = chapter.select("span.chapter-view").text() ?: null,
                            uploadedAt = chapter.select("span.chapter-time").attr("title") ?: null,
                            chapterUrl = chapter.select("a").attr("href") ?: null
                        )
                    )
                }
            } ?: chapterList.add(
                ChapterModel(
                    title = null,
                    view = null,
                    uploadedAt = null,
                    chapterUrl = null
                )
            )
        }

        return Result.success(
            MangaModel(
                title = doc.getElementsByClass("story-info-right").select("h1").text() ?: null,
                thumbnail = doc.getElementsByClass("story-info-left").select("span.info-image").select("img").attr("src") ?: null,
                authors = authors,
                genres = genres,
                status = status,
                updatedAt = updatedAt,
                description = doc.getElementsByClass("panel-story-info").select("div.panel-story-info-description").text().split(":", limit = 2)[1].trim(),
                view = view,
                rating = doc.getElementById("rate_row_cmd")?.text()?.split(":")?.get(1)?.trim()?.split("/")?.get(0)?.trim(),
                mangaUrl = mangaUrl,
                chapterList = chapterList
            )
        )
    }
}