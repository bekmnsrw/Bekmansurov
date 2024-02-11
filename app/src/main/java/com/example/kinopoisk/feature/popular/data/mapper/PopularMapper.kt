package com.example.kinopoisk.feature.popular.data.mapper

import com.example.kinopoisk.feature.popular.data.response.FilmBriefResponse
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief

fun FilmBriefResponse.toFilmBrief(): FilmBrief = FilmBrief(
    filmId = filmId,
    nameRu = nameRu,
    posterUrlPreview = posterUrlPreview,
    year = year.toString(),
    genre = genres.first().genre.replaceFirstChar { it.uppercase() }
)

fun List<FilmBriefResponse>.toFilmBriefList(): List<FilmBrief> = this.map { it.toFilmBrief() }
