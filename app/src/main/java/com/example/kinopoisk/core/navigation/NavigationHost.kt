package com.example.kinopoisk.core.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.core.navigation.navgraph.NavigationGraph
import com.example.kinopoisk.core.navigation.navgraph.popularNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    val bottomAppBarItems = listOf(
        BottomAppBarItem.Popular,
        BottomAppBarItem.Favorites
    )

    val screensToShowBottomAppBar = listOf(
        POPULAR_SCREEN_ROUTE,
        FAVORITES_SCREEN_ROUTE
    )

    val showBottomBar =
        navController.currentBackStackEntryAsState().value?.destination?.route in screensToShowBottomAppBar

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    bottomAppBarItems = bottomAppBarItems
                )
            }
        },
        containerColor = KinopoiskTheme.kinopoiskColor.background
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.PopularNavGraph.route
        ) {
            popularNavGraph(navController = navController)
        }
    }
}
