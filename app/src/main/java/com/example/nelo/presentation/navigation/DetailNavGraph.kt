package com.example.nelo.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nelo.MainViewModel
import com.example.nelo.presentation.screen.chapterview.PageListView
import com.example.nelo.presentation.screen.mangaview.MangaScreen

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
        ) { backStackEntry ->
            MangaScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                mangaDetailsViewModel = hiltViewModel(backStackEntry)
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