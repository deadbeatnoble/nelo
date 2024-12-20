package com.example.nelo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nelo.MainViewModel
import com.example.nelo.presentation.screen.browse.BrowseScreen
import com.example.nelo.presentation.screen.history.HistoryScreen
import com.example.nelo.presentation.screen.library.LibraryScreen
import com.example.nelo.presentation.screen.mangaview.MangaScreen

@Composable
fun BottomNavGraph(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = RootNavGraphs.BottomGraph.route,
        startDestination = BottomNavScreens.Browse.route
    ) {
        composable(
            route = BottomNavScreens.Library.route
        ) {
            LibraryScreen(
                mainViewModel = mainViewModel,
                navController = navController
            )
        }
        composable(
            route = BottomNavScreens.Browse.route
        ) {
            BrowseScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        composable(
            route = BottomNavScreens.History.route
        ) {
            HistoryScreen(
                mainViewModel = mainViewModel,
                navController = navController
            )
        }
        detailNavGraph(
            navController = navController,
            mainViewModel = mainViewModel
        )
    }
}

