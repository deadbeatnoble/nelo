package com.example.nelo.model

data class ChapterResponseModel(
    val response: String,
    val data: List<PageModel>,
    val referrer: String
)
