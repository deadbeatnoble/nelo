package com.example.nelo.model

data class ChapterModel(
    val title: String?,
    val view: String?,
    val uploadedAt: String?,
    val chapterUrl: String?,
    val pages: List<PageModel> = emptyList()
)