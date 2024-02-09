package com.example.kinopoisk.core.navigation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.kinopoisk.core.navigation.POPULAR_SCREEN_ROUTE
import com.example.kinopoisk.feature.popular.presentation.PopularScreen

fun NavGraphBuilder.favoritesNavGraph(navController: NavController) {
    navigation(
        startDestination = POPULAR_SCREEN_ROUTE,
        route = NavigationGraph.PopularNavGraph.route
    ) {
        composable(
            route = POPULAR_SCREEN_ROUTE
        ) {
            PopularScreen(navController = navController)
        }
    }
}
