package com.example.kinopoisk.feature.popular.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryResponse(
    @SerialName("country") val country: String
)
