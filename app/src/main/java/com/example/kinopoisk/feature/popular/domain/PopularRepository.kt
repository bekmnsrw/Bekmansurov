package com.example.kinopoisk.feature.popular.domain

import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import kotlinx.coroutines.flow.Flow

interface PopularRepository {

    suspend fun getTop100Films(): Flow<List<FilmBrief>>
}
