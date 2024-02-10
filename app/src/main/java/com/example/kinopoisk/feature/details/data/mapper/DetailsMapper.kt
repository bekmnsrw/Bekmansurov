package com.example.kinopoisk.feature.details.data.mapper

import com.example.kinopoisk.feature.details.data.response.CountryResponse
import com.example.kinopoisk.feature.details.data.response.FilmDetailsResponse
import com.example.kinopoisk.feature.details.data.response.GenreResponse
import com.example.kinopoisk.feature.details.domain.dto.Country
import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import com.example.kinopoisk.feature.details.domain.dto.Genre

fun CountryResponse.toCountry(): Country = Country(country = country)

fun List<CountryResponse>.toCountriesList(): List<Country> = this.map { it.toCountry() }

fun GenreResponse.toGenre(): Genre = Genre(genre = genre)

fun List<GenreResponse>.toGenresList(): List<Genre> = this.map { it.toGenre() }

fun FilmDetailsResponse.toFilmDetails(): FilmDetails = FilmDetails(
    filmId = kinopoiskId,
    nameRu = nameRu,
    description = description,
    genres = genres.toGenresList().joinToString(", ") { it.genre },
    countries = countries.toCountriesList().joinToString(", ") { it.country },
    imageUrl = posterUrl
)
