package com.example.kinopoisk.feature.details.domain

import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    suspend fun getFilmDetails(filmId: Int): Flow<FilmDetails>
}
