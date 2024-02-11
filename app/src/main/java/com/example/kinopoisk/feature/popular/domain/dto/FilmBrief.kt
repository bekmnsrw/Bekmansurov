package com.example.kinopoisk.feature.popular.domain.dto

data class FilmBrief(
    val filmId: Int,
    val nameRu: String,
    val posterUrlPreview: String,
    val year: String,
    val genre: String
)
