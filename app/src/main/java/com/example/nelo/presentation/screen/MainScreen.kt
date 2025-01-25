package com.example.nelo.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nelo.MainViewModel
import com.example.nelo.R
import com.example.nelo.presentation.navigation.BottomNavGraph
import com.example.nelo.presentation.navigation.BottomNavScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavScreens.Browse,
        BottomNavScreens.Library,
        BottomNavScreens.History
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomNavigationBar = screens.any { screen ->
        screen.route == currentDestination?.route
    }

    Scaffold(
        bottomBar = {
            if (showBottomNavigationBar) {
                BottomNavigationBar(
                    screens = screens,
                    selectedItem = currentDestination?.route!!,
                    onItemSelected = {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
            //BottomBar(navController = navController)
        }
    ) {
        BottomNavGraph(
            mainViewModel = mainViewModel,
            navController = navController
        )
    }
}

@Composable
fun BottomNavigationBar(
    screens: List<BottomNavScreens>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(18.dp))
                .background(colorResource(R.color.blue_bg))
                .padding(horizontal = 32.dp, vertical = 10.dp)
        ) {
            screens.forEach {
                val isSelected = selectedItem == it.route

                if (isSelected) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .background(colorResource(R.color.orange_bg))
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                            .clickable {
                                onItemSelected.invoke(it.route)
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = it.selectedIcon),
                            contentDescription = it.route,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { onItemSelected.invoke(it.route) }) {
                        Icon(
                            painter = painterResource(id = it.unselectedIcon),
                            contentDescription = it.route,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            }
        }
    }
}