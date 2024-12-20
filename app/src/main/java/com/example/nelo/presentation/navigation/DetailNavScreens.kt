package com.example.nelo.presentation.navigation

sealed class DetailNavScreens(
    val route: String
) {
    object MangaScreen: DetailNavScreens(route = "manga")
    object ChapterScreen: DetailNavScreens(route = "chapter")
}