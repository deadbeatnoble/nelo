package com.example.nelo.presentation.navigation

import com.example.nelo.R

sealed class BottomNavScreens(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Library: BottomNavScreens(
        route = "library",
        title = "Library",
        icon = R.drawable.library
    )
    object Browse: BottomNavScreens(
        route = "browse",
        title = "Browse",
        icon = R.drawable.browse
    )
    object History: BottomNavScreens(
        route = "history",
        title = "History",
        icon = R.drawable.history
    )
}