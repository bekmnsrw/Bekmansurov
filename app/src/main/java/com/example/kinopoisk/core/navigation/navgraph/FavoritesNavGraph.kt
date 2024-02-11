package com.example.kinopoisk.core.navigation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.kinopoisk.core.navigation.FAVORITES_SCREEN_ROUTE
import com.example.kinopoisk.core.navigation.NestedScreen
import com.example.kinopoisk.feature.details.presentation.DetailsScreen
import com.example.kinopoisk.feature.favorites.presentation.FavoritesScreen
import com.example.kinopoisk.feature.search.presentation.SearchScreen

fun NavGraphBuilder.favoritesNavGraph(navController: NavController) {
    navigation(
        startDestination = FAVORITES_SCREEN_ROUTE,
        route = NavigationGraph.FavoritesNavGraph.route
    ) {
        composable(
            route = FAVORITES_SCREEN_ROUTE
        ) {
            FavoritesScreen(navController = navController)
        }

        composable(
            route = NestedScreen.FilmDetails.route2
        ) {
            DetailsScreen(navController = navController)
        }

        composable(
            route = NestedScreen.SearchScreen.route2
        ) {
            SearchScreen(navController = navController)
        }
    }
}
