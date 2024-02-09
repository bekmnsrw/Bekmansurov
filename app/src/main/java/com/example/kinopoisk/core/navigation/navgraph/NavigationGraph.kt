package com.example.kinopoisk.core.navigation.navgraph

import com.example.kinopoisk.core.navigation.FAVORITES_GRAPH_ROUTE
import com.example.kinopoisk.core.navigation.POPULAR_GRAPH_ROUTE

sealed class NavigationGraph(val route: String) {

    data object PopularNavGraph : NavigationGraph(route = POPULAR_GRAPH_ROUTE)
    data object FavoritesNavGraph : NavigationGraph(route = FAVORITES_GRAPH_ROUTE)
}
