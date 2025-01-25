package com.example.nelo.presentation.navigation

import com.example.nelo.R

sealed class BottomNavScreens(
    val route: String,
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Library: BottomNavScreens(
        route = "library",
        title = "Library",
        selectedIcon = R.drawable.selected_library,
        unselectedIcon = R.drawable.unselected_library
    )
    object Browse: BottomNavScreens(
        route = "browse",
        title = "Browse",
        selectedIcon = R.drawable.selected_home,
        unselectedIcon = R.drawable.unselected_home
    )
    object History: BottomNavScreens(
        route = "history",
        title = "History",
        selectedIcon = R.drawable.selected_history,
        unselectedIcon = R.drawable.unselected_history
    )
}