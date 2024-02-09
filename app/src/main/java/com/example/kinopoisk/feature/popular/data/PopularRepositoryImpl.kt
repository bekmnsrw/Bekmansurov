package com.example.kinopoisk.feature.popular.data

import com.example.kinopoisk.core.network.KinopoiskApi
import com.example.kinopoisk.feature.popular.data.mapper.toFilmBriefList
import com.example.kinopoisk.feature.popular.domain.PopularRepository
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PopularRepositoryImpl(
    private val kinopoiskApi: KinopoiskApi
) : PopularRepository {

    override suspend fun getTop100Films(): Flow<List<FilmBrief>> = flow {
        emit(
            kinopoiskApi.getTop100Films()
                .films
                .toFilmBriefList()
        )
    }
}
