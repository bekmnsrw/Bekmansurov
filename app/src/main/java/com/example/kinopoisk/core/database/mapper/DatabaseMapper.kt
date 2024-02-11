package com.example.kinopoisk.core.database.mapper

import com.example.kinopoisk.core.database.entity.FavoriteFilmEntity
import com.example.kinopoisk.feature.favorites.domain.dto.FavoriteFilm
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief

fun FilmBrief.toFavoriteFilmEntity(): FavoriteFilmEntity = FavoriteFilmEntity(
    kinopoiskId = filmId,
    name = nameRu,
    posterUrl = posterUrlPreview,
    genre = genre,
    year = year
)

fun FavoriteFilmEntity.toFavoriteFilm(): FavoriteFilm = FavoriteFilm(
    id = id,
    kinopoiskId = kinopoiskId,
    name = name,
    posterUrl = posterUrl,
    year = year,
    genre = genre
)

fun List<FavoriteFilmEntity>.toFavoriteFilmList(): List<FavoriteFilm> = this.map { it.toFavoriteFilm() }

fun FavoriteFilm.toFilmBrief(): FilmBrief = FilmBrief(
    filmId = kinopoiskId,
    nameRu = name,
    posterUrlPreview = posterUrl,
    year = year,
    genre = genre
)

fun List<FavoriteFilm>.toFilmBriefList(): List<FilmBrief> = this.map { it.toFilmBrief() }
