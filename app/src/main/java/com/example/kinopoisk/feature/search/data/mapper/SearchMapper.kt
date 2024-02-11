package com.example.kinopoisk.feature.search.data.mapper

import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.search.data.response.FilmResponse
import com.example.kinopoisk.feature.search.domain.dto.SearchResult

private const val UNKNOWN = "?"

fun FilmResponse.toSearchResult(): SearchResult = SearchResult(
    filmId = filmId,
    nameRu = nameRu ?: nameEn ?: UNKNOWN,
    genre = genres.first().genre.replaceFirstChar { it.uppercase() },
    year = year ?: UNKNOWN,
    imageUrl = posterUrlPreview
)

fun List<FilmResponse>.toSearchResultList(): List<SearchResult> = this.map { it.toSearchResult() }

fun SearchResult.toFilmBrief(): FilmBrief = FilmBrief(
    filmId = filmId,
    nameRu = nameRu,
    posterUrlPreview = imageUrl,
    year = year,
    genre = genre
)

fun List<SearchResult>.toFilmBriefList(): List<FilmBrief> = this.map { it.toFilmBrief() }
