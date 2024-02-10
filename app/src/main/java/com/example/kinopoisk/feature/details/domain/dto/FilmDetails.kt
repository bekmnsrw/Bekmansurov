package com.example.kinopoisk.feature.details.domain.dto

data class FilmDetails(
    val filmId: Int,
    val nameRu: String,
    val description: String,
    val genres: String,
    val countries: String,
    val imageUrl: String
)
