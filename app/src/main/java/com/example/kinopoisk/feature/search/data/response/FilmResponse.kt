package com.example.kinopoisk.feature.search.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmResponse(
    @SerialName("countries") val countries: List<CountryResponse>,
    @SerialName("description") val description: String?,
    @SerialName("filmId") val filmId: Int,
    @SerialName("filmLength") val filmLength: String?,
    @SerialName("genres") val genres: List<GenreResponse>,
    @SerialName("nameEn") val nameEn: String?,
    @SerialName("nameRu") val nameRu: String?,
    @SerialName("posterUrl") val posterUrl: String?,
    @SerialName("posterUrlPreview") val posterUrlPreview: String,
    @SerialName("rating") val rating: String?,
    @SerialName("ratingVoteCount") val ratingVoteCount: Int?,
    @SerialName("type") val type: String?,
    @SerialName("year") val year: String?
)
