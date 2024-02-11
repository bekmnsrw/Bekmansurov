package com.example.kinopoisk.feature.favorites.domain.usecase

import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository
import com.example.kinopoisk.feature.favorites.domain.dto.FavoriteFilm
import kotlinx.coroutines.flow.Flow

class GetAllFavoriteFilmsUseCase(
    private val favoritesRepository: FavoritesRepository
) {

    suspend operator fun invoke(): Flow<List<FavoriteFilm>> = favoritesRepository.getAllFavoriteFilms()
}
