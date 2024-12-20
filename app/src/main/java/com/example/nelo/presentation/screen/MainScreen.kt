package com.example.nelo.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nelo.MainViewModel
import com.example.nelo.presentation.navigation.BottomNavGraph
import com.example.nelo.presentation.navigation.BottomNavScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        BottomNavGraph(
            mainViewModel = mainViewModel,
            navController = navController
        )
    }
}


@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavScreens.Library,
        BottomNavScreens.Browse,
        BottomNavScreens.History
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomNavigationBar = screens.any { screen ->
        screen.route == currentDestination?.route
    }

    if (showBottomNavigationBar){
        BottomNavigation(
            backgroundColor = Color.Red
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreens,
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val color = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) Color.White else Color.Black

    BottomNavigationItem(
        icon = {
            Icon(
                painter = painterResource(screen.icon),
                contentDescription = "Bottom Navigation Icon",
                tint = color,
                modifier = Modifier
                    .size(25.dp)
            )
        },
        label = {
            Text(
                text = screen.title
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = Color.White,
        unselectedContentColor = Color.Black,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Preview
@Composable
fun BottomNavPreview() {
    BottomBar(navController = rememberNavController())
}