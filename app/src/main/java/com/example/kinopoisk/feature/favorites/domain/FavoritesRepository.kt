package com.example.kinopoisk.feature.favorites.domain

import com.example.kinopoisk.feature.favorites.domain.dto.FavoriteFilm
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun saveFavoriteFilm(filmBrief: FilmBrief): Flow<Unit>

    suspend fun deleteFavoriteFilmById(kinopoiskId: Int): Flow<Unit>

    suspend fun getAllFavoriteFilms(): Flow<List<FavoriteFilm>>

    suspend fun getFavoriteFilmById(kinopoiskId: Int): Flow<FavoriteFilm>

    suspend fun searchFavoriteFilms(query: String): Flow<List<FavoriteFilm>>
}
