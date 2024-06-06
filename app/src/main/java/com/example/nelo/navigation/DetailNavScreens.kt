package com.example.nelo.navigation

sealed class DetailNavScreens(
    val route: String
) {
    object MangaScreen: DetailNavScreens(route = "manga")
    object ChapterScreen: DetailNavScreens(route = "chapter")
}