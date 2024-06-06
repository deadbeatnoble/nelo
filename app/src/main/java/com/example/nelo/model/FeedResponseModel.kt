package com.example.nelo.model

data class FeedResponseModel(
    val response: String,
    val data: List<MangaModel>,
    val limit: Int,
    val total: Int,
    val page: Int,
    val hasNextPage: Boolean
)
