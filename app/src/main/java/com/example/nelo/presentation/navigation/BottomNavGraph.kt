package com.example.nelo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nelo.MainViewModel
import com.example.nelo.presentation.viewmodels.HistoryViewModel
import com.example.nelo.presentation.screen.browse.BrowseScreen
import com.example.nelo.presentation.screen.history.HistoryScreen
import com.example.nelo.presentation.screen.library.LibraryScreen
import com.example.nelo.presentation.viewmodels.LibraryViewModel
import com.example.nelo.presentation.viewmodels.SharedViewModel

@Composable
fun BottomNavGraph(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel(),
    libraryViewModel: LibraryViewModel = hiltViewModel()
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
                navController = navController,
                libraryViewModel = libraryViewModel
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
                navController = navController,
                sharedViewModel = sharedViewModel,
                historyViewModel = historyViewModel
            )
        }
        detailNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            sharedViewModel = sharedViewModel,
            historyViewModel = historyViewModel,
            libraryViewModel = libraryViewModel
        )
    }
}

