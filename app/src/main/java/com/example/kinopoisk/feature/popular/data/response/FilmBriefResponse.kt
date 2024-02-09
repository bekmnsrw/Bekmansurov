package com.example.kinopoisk.feature.popular.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmBriefResponse(
    @SerialName("countries") val countries: List<Country>,
    @SerialName("genres") val genres: List<Genre>,
    @SerialName("filmId") val filmId: Int,
    @SerialName("nameEn") val nameEn: String?,
    @SerialName("nameRu") val nameRu: String,
    @SerialName("filmLength") val filmLength: String?,
    @SerialName("isAfisha") val isAfisha: Int,
    @SerialName("posterUrl") val posterUrl: String,
    @SerialName("posterUrlPreview") val posterUrlPreview: String,
    @SerialName("year") val year: Int,
    @SerialName("rating") val rating: String,
    @SerialName("ratingVoteCount") val ratingVoteCount: Int
)
