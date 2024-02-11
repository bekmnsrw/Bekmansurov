package com.example.kinopoisk.core.navigation

sealed class NestedScreen(
    val route1: String,
    val route2: String,
    val route3: String
) {

    private companion object {
        const val FILM_ID = "filmId"
        const val SOURCE = "source"
    }

    data object FilmDetails : NestedScreen(
        route1 = "$DETAILS_FROM_POPULAR/{$FILM_ID}/{$SOURCE}",
        route2 = "$DETAILS_FROM_FAVORITES/{$FILM_ID}/{$SOURCE}",
        route3 = "$DETAILS_FROM_SEARCH/{$FILM_ID}/{$SOURCE}"
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
        route1 = "$SEARCH_FROM_POPULAR/{$SOURCE}",
        route2 = "$SEARCH_FROM_FAVORITES/{$SOURCE}",
        route3 = ""
    ) {

        fun fromPopularScreen(source: String) = "$SEARCH_FROM_POPULAR/$source"

        fun fromFavoritesScreen(source: String) = "$SEARCH_FROM_FAVORITES/$source"
    }
}
