package com.example.kinopoisk.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kinopoisk.core.database.entity.FavoriteFilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteFilm(favoriteFilmEntity: FavoriteFilmEntity)

    @Query(value = "DELETE FROM favorite_film WHERE kinopoisk_id = :kinopoiskId")
    suspend fun deleteFavoriteFilmById(kinopoiskId: Int)

    @Query(value = "SELECT * FROM favorite_film ORDER BY id DESC")
    fun getAllFavoriteFilmsOrderedByIdDesc(): Flow<List<FavoriteFilmEntity>>

    @Query(value = "SELECT * FROM favorite_film WHERE kinopoisk_id = :kinopoiskId")
    fun getFavoriteFilmById(kinopoiskId: Int): Flow<FavoriteFilmEntity>

    @Query(value = "SELECT * FROM favorite_film WHERE name LIKE '%' || :query || '%'")
    fun searchFavoriteFilms(query: String): Flow<List<FavoriteFilmEntity>>
}
