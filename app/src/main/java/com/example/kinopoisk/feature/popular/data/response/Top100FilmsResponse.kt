package com.example.kinopoisk.feature.popular.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Top100FilmsResponse(
    @SerialName("films") val films: List<FilmBriefResponse>,
    @SerialName("pagesCount") val pagesCount: Int
)
