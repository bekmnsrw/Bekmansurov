package com.example.kinopoisk.feature.details.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("genre") val genre: String
)
