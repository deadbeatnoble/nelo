package com.example.nelo.data.remote.scraper

import com.example.nelo.domain.model.AuthorModel
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.FeedResponseModel
import com.example.nelo.domain.model.GenreModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.domain.model.PageModel
import org.jsoup.nodes.Document

class Parser {
    fun mangaDetailsParser(doc: Document, mangaUrl: String): Result<MangaModel> {
        return Result.success(
            MangaModel(
                title = doc.select("ul.manga-info-text h1").firstOrNull()?.text()?.trim(),
                thumbnail = doc.select("div.manga-info-top div.manga-info-pic img").firstOrNull()?.attr("src"),
                authors = doc.select("ul.manga-info-text li")[1]?.select("a")?.map {
                    AuthorModel(
                        name = it.text()?.trim(),
                        url = it.attr("href")?.trim()
                    )
                } ?: emptyList(),
                genres = doc.select("ul.manga-info-text li.genres a")?.map {
                    GenreModel(
                        name = it.text()?.trim(),
                        id = it.attr("href")?.split("/")?.lastOrNull()?.trim(),
                        url = it.attr("href")?.trim()
                    )
                } ?: emptyList(),
                status = doc.select("ul.manga-info-text li:contains(Status)").firstOrNull()?.text()?.split(":")?.get(1)?.trim(),
                updatedAt = doc.select("ul.manga-info-text li:contains(Last updated)").firstOrNull()?.text()?.split(":")?.get(1)?.trim(),
                description = doc.select("div#contentBox").firstOrNull()?.ownText()?.trim(),
                view = doc.select("ul.manga-info-text li:contains(View)").firstOrNull()?.text()?.split(":")?.get(1)?.trim(),
                rating = doc.select("em#rate_row_cmd").firstOrNull()?.text()?.split(":")?.get(1)?.trim()?.split("/")?.get(0)?.trim(),
                mangaUrl = mangaUrl,
                chapterList = doc.select("div.chapter-list div.row")?.map {
                    ChapterModel(
                        title = it.select("a").text(),
                        view = it.select("span:nth-of-type(2)").text(),
                        uploadedAt = it.select("span:nth-of-type(3)").attr("title"),
                        chapterUrl = it.select("a").attr("href")
                    )
                } ?: emptyList()
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

    fun feedParser(doc: Document): Result<FeedResponseModel> {
        return Result.success(
            FeedResponseModel(
                response = "ok",
                data = doc.select("div.truyen-list div.list-truyen-item-wrap").map {
                    MangaModel(
                        title = it.select("h3 a").firstOrNull()?.text()?.trim(),
                        thumbnail = it.select("a img").firstOrNull()?.attr("src"),
                        authors = emptyList(),
                        genres = emptyList(),
                        status = null,
                        updatedAt = "",
                        description = it.select("p").firstOrNull()?.text()?.trim(),
                        view = it.select("span.aye_icon").firstOrNull()?.text()?.trim(),
                        rating = "??",
                        mangaUrl = it.select("a").firstOrNull()?.attr("href"),
                        chapterList = emptyList()
                    )},
                limit = doc.select("div.truyen-list").firstOrNull()?.select("div")?.size?.dec() ?: 0,
                total = doc.select("div.panel_page_number div.group_qty a.page_blue").firstOrNull()?.text()?.split(" ")?.get(1)?.trim()?.replace(",", "")?.toInt() ?: doc.select("div.truyen-list").firstOrNull()?.select("div")?.size?.dec() ?: 0,
                page = doc.select("div.panel_page_number div.group_page a.page_select").firstOrNull()?.text()?.trim()?.toInt() ?: 1,
                hasNextPage = (doc.select("div.panel_page_number div.group_page a.page_select").firstOrNull()?.text()?.trim()?.toInt() ?: 0) != (doc.select("div.panel_page_number div.group_page a.page_last").firstOrNull()?.text()?.split("(")?.get(1)?.replace(")", "")?.toInt() ?: 0)
            )
        )
    }
}