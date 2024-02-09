package com.example.kinopoisk.core.network

import com.example.kinopoisk.feature.popular.data.response.Top100FilmsResponse
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

    @GET(value = "v2.2/films/top/{film-id}")
    suspend fun getFilmDescription(
        @Path(value = "film-id") filmId: Int
    )
}
