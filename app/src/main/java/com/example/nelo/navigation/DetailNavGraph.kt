package com.example.nelo.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nelo.MainViewModel
import com.example.nelo.screen.chapterview.PageListView
import com.example.nelo.screen.mangaview.MangaScreen

fun NavGraphBuilder.detailNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = RootNavGraphs.DetailGraph.route,
        startDestination = DetailNavScreens.MangaScreen.route
    ) {
        composable(
            route = DetailNavScreens.MangaScreen.route
        ) {
            MangaScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = DetailNavScreens.ChapterScreen.route
        ) {
            PageListView(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}