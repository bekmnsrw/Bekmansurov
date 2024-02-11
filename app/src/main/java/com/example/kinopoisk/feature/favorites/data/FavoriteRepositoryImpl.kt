package com.example.kinopoisk.feature.favorites.data

import com.example.kinopoisk.core.database.AppDatabase
import com.example.kinopoisk.core.database.mapper.toFavoriteFilm
import com.example.kinopoisk.core.database.mapper.toFavoriteFilmEntity
import com.example.kinopoisk.core.database.mapper.toFavoriteFilmList
import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository
import com.example.kinopoisk.feature.favorites.domain.dto.FavoriteFilm
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase
) : FavoritesRepository {

    override suspend fun saveFavoriteFilm(filmBrief: FilmBrief): Flow<Unit> {
        appDatabase
            .favoriteFilmDao()
            .saveFavoriteFilm(favoriteFilmEntity = filmBrief.toFavoriteFilmEntity())

        return flowOf(Unit)
    }

    override suspend fun deleteFavoriteFilmById(kinopoiskId: Int): Flow<Unit> {
        appDatabase
            .favoriteFilmDao()
            .deleteFavoriteFilmById(kinopoiskId = kinopoiskId)

        return flowOf(Unit)
    }

    override suspend fun getAllFavoriteFilms(): Flow<List<FavoriteFilm>> {
        return appDatabase
            .favoriteFilmDao()
            .getAllFavoriteFilmsOrderedByIdDesc()
            .map { it.toFavoriteFilmList() }
    }

    override suspend fun getFavoriteFilmById(kinopoiskId: Int): Flow<FavoriteFilm> {
        return appDatabase
            .favoriteFilmDao()
            .getFavoriteFilmById(kinopoiskId = kinopoiskId)
            .map { it.toFavoriteFilm() }
    }

    override suspend fun searchFavoriteFilms(query: String): Flow<List<FavoriteFilm>> {
        return appDatabase
            .favoriteFilmDao()
            .searchFavoriteFilms(query = query)
            .map { it.toFavoriteFilmList() }
    }
}
