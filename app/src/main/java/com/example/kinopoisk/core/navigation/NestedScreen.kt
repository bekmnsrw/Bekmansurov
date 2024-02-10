package com.example.kinopoisk.core.navigation

sealed class NestedScreen(
    val route1: String,
    val route2: String
) {

    private companion object {
        const val FILM_ID = "filmId"
    }

    data object FilmDetails : NestedScreen(
        route1 = "$DETAILS_FROM_POPULAR/{$FILM_ID}",
        route2 = "$DETAILS_FROM_FAVORITES/{$FILM_ID}"
    ) {

        fun fromPopularScreen(
            filmId: Int
        ): String = "$DETAILS_FROM_POPULAR/$filmId"

        fun fromFavoritesScreen(
            filmId: Int
        ): String = "$DETAILS_FROM_FAVORITES/$filmId"
    }
}
