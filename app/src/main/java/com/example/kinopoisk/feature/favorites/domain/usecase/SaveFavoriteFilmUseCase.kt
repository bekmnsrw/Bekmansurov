package com.example.kinopoisk.feature.favorites.domain.usecase

import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief

class SaveFavoriteFilmUseCase(
    private val favoritesRepository: FavoritesRepository
) {

    suspend operator fun invoke(
        filmBrief: FilmBrief
    ) = favoritesRepository.saveFavoriteFilm(filmBrief = filmBrief)
}
