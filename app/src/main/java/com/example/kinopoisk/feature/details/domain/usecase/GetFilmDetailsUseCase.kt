package com.example.kinopoisk.feature.details.domain.usecase

import com.example.kinopoisk.feature.details.domain.DetailsRepository
import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import kotlinx.coroutines.flow.Flow

class GetFilmDetailsUseCase(
    private val detailsRepository: DetailsRepository
) {

    suspend operator fun invoke(
        filmId: Int
    ): Flow<FilmDetails> = detailsRepository.getFilmDetails(filmId = filmId)
}
