package com.example.kinopoisk.feature.details.data

import com.example.kinopoisk.core.network.KinopoiskApi
import com.example.kinopoisk.feature.details.data.mapper.toFilmDetails
import com.example.kinopoisk.feature.details.domain.DetailsRepository
import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailsRepositoryImpl(
    private val kinopoiskApi: KinopoiskApi
) : DetailsRepository {

    override suspend fun getFilmDetails(filmId: Int): Flow<FilmDetails> = flow {
        emit(
            kinopoiskApi.getFilmDetails(filmId = filmId)
                .toFilmDetails()
        )
    }
}
