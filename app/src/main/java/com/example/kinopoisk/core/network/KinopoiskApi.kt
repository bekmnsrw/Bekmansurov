package com.example.kinopoisk.core.network

import com.example.kinopoisk.feature.details.data.response.FilmDetailsResponse
import com.example.kinopoisk.feature.popular.data.response.Top100FilmsResponse
import com.example.kinopoisk.feature.search.data.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    private companion object {
        const val TYPE = "TOP_100_POPULAR_FILMS"
    }

    @GET(value = "v2.2/films/top")
    suspend fun getTop100Films(
        @Query(value = "type") type: String = TYPE
    ): Top100FilmsResponse

    @GET(value = "v2.2/films/{film-id}")
    suspend fun getFilmDetails(
        @Path(value = "film-id") filmId: Int
    ): FilmDetailsResponse

    @GET(value = "v2.1/films/search-by-keyword")
    suspend fun searchFilm(
        @Query(value = "keyword") query: String
    ): SearchResponse
}
