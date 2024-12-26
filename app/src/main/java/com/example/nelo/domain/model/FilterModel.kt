package com.example.nelo.domain.model

data class FilterModel(
    val include: List<Int>,
    val exclude: List<Int>,
    val status: String,
    val orderBy: String,
    val keyType: String,
    val keyWord: String
)