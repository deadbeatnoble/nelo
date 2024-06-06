package com.example.nelo.navigation

sealed class RootNavGraphs(
    val route: String
)  {
    object BottomGraph: RootNavGraphs(route = "bottom")
    object DetailGraph: RootNavGraphs(route = "detail")
}