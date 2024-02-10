package com.example.kinopoisk.core.navigation

import com.example.kinopoisk.core.navigation.navgraph.NavigationGraph

sealed class BottomAppBarItem(
    val route: String,
    val text: String
) {

    data object Popular : BottomAppBarItem(
        route = NavigationGraph.PopularNavGraph.route,
        text = NavigationGraph.PopularNavGraph.name
    )

    data object Favorites : BottomAppBarItem(
        route = NavigationGraph.FavoritesNavGraph.route,
        text = NavigationGraph.FavoritesNavGraph.name
    )
}
