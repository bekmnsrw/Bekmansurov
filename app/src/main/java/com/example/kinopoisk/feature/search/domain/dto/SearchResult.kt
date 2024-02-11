package com.example.kinopoisk.feature.search.domain.dto

data class SearchResult(
    val filmId: Int,
    val nameRu: String,
    val genre: String,
    val year: String,
    val imageUrl: String
)
