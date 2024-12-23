package com.example.nelo.data.remote.scraper

import com.example.nelo.domain.model.AuthorModel
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.GenreModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.model.PageModel
import org.jsoup.nodes.Document

class Parser {
    fun mangaDetailsParser(doc: Document, mangaUrl: String): Result<MangaModel> {
        return Result.success(
            MangaModel(
                title = doc.select("div.story-info-right h1").firstOrNull()?.text()?.trim(),
                thumbnail = doc.select("div.story-info-left img.img-loading").firstOrNull()?.attr("src"),
                authors = doc.select("tr").getOrNull(1)?.select("td.table-value a")?.map { AuthorModel(name = it.text() ?: null,url = it.attr("href") ?: null) } ?: emptyList(),
                genres = doc.select("tr").getOrNull(3)?.select("td.table-value a")?.map { GenreModel(name = it.text() ?: null, id = it.attr("href").split("-")[1], url = it.attr("href") ?: null) } ?: emptyList(),
                status = doc.select("tr").getOrNull(2)?.text()?.split(":")?.get(1)?.trim(),
                updatedAt = doc.select("div.story-info-right-extent p").eq(0).firstOrNull()?.text()?.trim(),
                description = doc.select("div.panel-story-info-description").firstOrNull()?.text()?.split(":", limit = 2)?.get(1)?.trim(),
                view = doc.select("div.story-info-right-extent p").eq(1).firstOrNull()?.text()?.split(":")?.get(1)?.trim(),
                rating = doc.select("em.rate_row_cmd").firstOrNull()?.text()?.split(":")?.get(1)?.trim()?.split("/")?.get(0)?.trim(),
                mangaUrl = mangaUrl,
                chapterList = doc.select("div.panel-story-chapter-list ul li").map { ChapterModel(title = it.select("a").text() ?: null, view = it.select("span.chapter-view").text() ?: null, uploadedAt = it.select("span.chapter-time").text() ?: null, chapterUrl = it.select("a").attr("href") ?: null) }
            )
        )
    }

    fun chapterDetailsParser(doc: Document, chapterTitle: String, referrer: String): Result<ChapterModel> {
        return Result.success(
            ChapterModel(
                title = chapterTitle,
                pages = doc.select("div.container-chapter-reader img").map { PageModel( title = it.attr("title") ?: null, pageImageUrl = it.attr("src") ?: null ) },
                chapterUrl = referrer
            )
        )
    }
}