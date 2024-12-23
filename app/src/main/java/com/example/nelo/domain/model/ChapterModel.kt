package com.example.nelo.domain.model

data class ChapterModel(
    val title: String?,
    val view: String? = null,
    val uploadedAt: String? = null,
    val chapterUrl: String?,
    val pages: List<PageModel> = emptyList()
)