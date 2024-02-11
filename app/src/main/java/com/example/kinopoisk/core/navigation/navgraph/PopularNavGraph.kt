package com.example.kinopoisk.core.navigation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.kinopoisk.core.navigation.NestedScreen
import com.example.kinopoisk.core.navigation.POPULAR_SCREEN_ROUTE
import com.example.kinopoisk.feature.details.presentation.DetailsScreen
import com.example.kinopoisk.feature.popular.presentation.PopularScreen
import com.example.kinopoisk.feature.search.presentation.SearchScreen

fun NavGraphBuilder.popularNavGraph(navController: NavController) {
    navigation(
        startDestination = POPULAR_SCREEN_ROUTE,
        route = NavigationGraph.PopularNavGraph.route
    ) {
        composable(
            route = POPULAR_SCREEN_ROUTE
        ) {
            PopularScreen(navController = navController)
        }

        composable(
            route = NestedScreen.FilmDetails.route1
        ) {
            DetailsScreen(navController = navController)
        }

        composable(
            route = NestedScreen.FilmDetails.route3
        ) {
            DetailsScreen(navController = navController)
        }

        composable(
            route = NestedScreen.SearchScreen.route1
        ) {
            SearchScreen(navController = navController)
        }
    }
}
