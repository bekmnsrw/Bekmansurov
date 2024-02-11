package com.example.kinopoisk.core.navigation

sealed class NestedScreen(
    val route1: String,
    val route2: String,
    val route3: String
) {

    private companion object {
        const val FILM_ID = "filmId"
    }

    data object FilmDetails : NestedScreen(
        route1 = "$DETAILS_FROM_POPULAR/{$FILM_ID}",
        route2 = "$DETAILS_FROM_FAVORITES/{$FILM_ID}",
        route3 = "$DETAILS_FROM_SEARCH/{$FILM_ID}"
    ) {

        fun fromPopularScreen(
            filmId: Int
        ): String = "$DETAILS_FROM_POPULAR/$filmId"

        fun fromFavoritesScreen(
            filmId: Int
        ): String = "$DETAILS_FROM_FAVORITES/$filmId"

        fun fromSearchScreen(
            filmId: Int
        ): String = "$DETAILS_FROM_SEARCH/$filmId"
    }

    data object SearchScreen : NestedScreen(
        route1 = SEARCH_FROM_POPULAR,
        route2 = SEARCH_FROM_FAVORITES,
        route3 = ""
    )
}
