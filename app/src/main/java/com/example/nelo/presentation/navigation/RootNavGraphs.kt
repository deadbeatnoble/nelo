package com.example.nelo.presentation.navigation

sealed class RootNavGraphs(
    val route: String
)  {
    object BottomGraph: RootNavGraphs(route = "bottom")
    object DetailGraph: RootNavGraphs(route = "detail")
}