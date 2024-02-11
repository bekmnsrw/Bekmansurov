package com.example.kinopoisk.feature.search.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("films") val films: List<FilmResponse>,
    @SerialName("keyword") val keyword: String,
    @SerialName("pagesCount") val pagesCount: Int,
    @SerialName("searchFilmsCountResult") val searchFilmsCountResult: Int
)
