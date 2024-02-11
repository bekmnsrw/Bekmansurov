package com.example.kinopoisk.core.navigation.navgraph

import com.example.kinopoisk.core.navigation.FAVORITES_GRAPH_ROUTE
import com.example.kinopoisk.core.navigation.POPULAR_GRAPH_ROUTE

private const val POPULAR = "Популярные"
private const val FAVORITES = "Избранное"

sealed class NavigationGraph(
    val route: String,
    val name: String
) {

    data object PopularNavGraph : NavigationGraph(
        route = POPULAR_GRAPH_ROUTE,
        name = POPULAR
    )

    data object FavoritesNavGraph : NavigationGraph(
        route = FAVORITES_GRAPH_ROUTE,
        name = FAVORITES
    )
}
