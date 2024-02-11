package com.example.kinopoisk.core.navigation

sealed class NestedScreen(
    val route1: String,
    val route2: String,
    val route3: String
) {

    data object FilmDetails : NestedScreen(
        route1 = "$DETAILS_FROM_POPULAR/{${NavArgs.FILM_ID.value}}/{${NavArgs.SOURCE.value}}",
        route2 = "$DETAILS_FROM_FAVORITES/{${NavArgs.FILM_ID.value}}/{${NavArgs.SOURCE.value}}",
        route3 = "$DETAILS_FROM_SEARCH/{${NavArgs.FILM_ID.value}}/{${NavArgs.SOURCE.value}}"
    ) {

        fun fromPopularScreen(
            filmId: Int,
            source: String
        ): String = "$DETAILS_FROM_POPULAR/$filmId/$source"

        fun fromFavoritesScreen(
            filmId: Int,
            source: String
        ): String = "$DETAILS_FROM_FAVORITES/$filmId/$source"

        fun fromSearchScreen(
            filmId: Int,
            source: String
        ): String = "$DETAILS_FROM_SEARCH/$filmId/$source"
    }

    data object SearchScreen : NestedScreen(
        route1 = "$SEARCH_FROM_POPULAR/{${NavArgs.SOURCE.value}}",
        route2 = "$SEARCH_FROM_FAVORITES/{${NavArgs.SOURCE.value}}",
        route3 = ""
    ) {

        fun fromPopularScreen(source: String) = "$SEARCH_FROM_POPULAR/$source"

        fun fromFavoritesScreen(source: String) = "$SEARCH_FROM_FAVORITES/$source"
    }
}
