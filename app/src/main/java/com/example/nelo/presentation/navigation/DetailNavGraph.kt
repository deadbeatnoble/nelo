package com.example.nelo.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.nelo.MainViewModel
import com.example.nelo.presentation.viewmodels.HistoryViewModel
import com.example.nelo.presentation.screen.chapterview.PageListView
import com.example.nelo.presentation.screen.mangaview.MangaScreen
import com.example.nelo.presentation.viewmodels.LibraryViewModel
import com.example.nelo.presentation.viewmodels.SharedViewModel

fun NavGraphBuilder.detailNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    sharedViewModel: SharedViewModel,
    historyViewModel: HistoryViewModel,
    libraryViewModel: LibraryViewModel
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
                mainViewModel = mainViewModel,
                sharedViewModel = sharedViewModel,
                historyViewModel = historyViewModel,
                libraryViewModel = libraryViewModel
            )
        }

        composable(
            route = "${DetailNavScreens.ChapterScreen.route}?chapterUrl={chapterUrl}&chapterTitle={chapterTitle}",
            arguments = listOf(
                navArgument("chapterUrl") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("chapterTitle") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            PageListView(
                navController = navController,
                mainViewModel = mainViewModel,
                chapterUrl = backStackEntry.arguments?.getString("chapterUrl") ?: "",
                chapterTitle = backStackEntry.arguments?.getString("chapterTitle") ?: "",
                sharedViewModel = sharedViewModel,
                historyViewModel = historyViewModel
            )
        }
    }
}