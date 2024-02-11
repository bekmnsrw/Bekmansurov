package com.example.kinopoisk.feature.favorites.domain.usecase

import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository
import com.example.kinopoisk.feature.favorites.domain.dto.FavoriteFilm
import kotlinx.coroutines.flow.Flow

class GetFavoriteFilmByIdUseCase(
    private val favoritesRepository: FavoritesRepository
) {

    suspend operator fun invoke(
        kinopoiskId: Int
    ): Flow<FavoriteFilm> = favoritesRepository.getFavoriteFilmById(kinopoiskId = kinopoiskId)
}
