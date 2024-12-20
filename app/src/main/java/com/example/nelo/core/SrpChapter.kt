package com.example.nelo.core

import com.example.nelo.domain.model.ChapterResponseModel
import com.example.nelo.domain.model.PageModel
import org.jsoup.nodes.Document

interface ChapterResponseListener {
    fun onSuccess(data: ChapterResponseModel)
    fun onFailure(message: String)
}

fun srpChapter(
    doc: Document,
    referrer: String,
    chapterResponseListener: ChapterResponseListener
) {
    val pageList = mutableListOf<PageModel>()

    doc.getElementsByClass("container-chapter-reader").firstOrNull()?.let {
        it.select("img").forEach { pageImage ->
            pageImage?.let {
                pageList.add(
                    PageModel(
                        title = pageImage.attr("title"),
                        pageImageUrl = pageImage.attr("src")
                    )
                )
            } ?: pageList.add(
                PageModel(
                    title = null,
                    pageImageUrl = null
                )
            )
        }
    } ?: chapterResponseListener.onFailure(
        message = "Unable to PARSE the page from"
    )

    chapterResponseListener.onSuccess(
        data = ChapterResponseModel(
            response = "ok",
            data = pageList,
            referrer = referrer
        )
    )
}