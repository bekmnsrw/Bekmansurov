package com.example.kinopoisk.feature.favorites.domain.usecase

import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository
import com.example.kinopoisk.feature.favorites.domain.dto.FavoriteFilm
import kotlinx.coroutines.flow.Flow

class SearchFavoriteFilmsUseCase(
    private val favoritesRepository: FavoritesRepository
) {

    suspend operator fun invoke(
        query: String
    ): Flow<List<FavoriteFilm>> = favoritesRepository.searchFavoriteFilms(query = query)
}
