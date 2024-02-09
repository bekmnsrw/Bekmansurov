package com.example.kinopoisk.feature.popular.domain.usecase

import com.example.kinopoisk.feature.popular.domain.PopularRepository
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import kotlinx.coroutines.flow.Flow

class GetTop100FilmsUseCase(
    private val popularRepository: PopularRepository
) {

    suspend operator fun invoke(): Flow<List<FilmBrief>> = popularRepository.getTop100Films()
}
