package com.example.kinopoisk.feature.favorites.domain.dto

data class FavoriteFilm(
    val id: Int,
    val kinopoiskId: Int,
    val name: String,
    val posterUrl: String,
    val year: String,
    val genre: String
)
