package com.example.kinopoisk.feature.favorites.domain.usecase

import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository

class DeleteFavoriteFilmByIdUseCase(
    private val favoritesRepository: FavoritesRepository
) {

    suspend operator fun invoke(
        kinopoiskId: Int
    ) = favoritesRepository.deleteFavoriteFilmById(kinopoiskId = kinopoiskId)
}
