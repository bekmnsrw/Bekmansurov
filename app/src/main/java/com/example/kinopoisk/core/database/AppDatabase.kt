package com.example.kinopoisk.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kinopoisk.core.database.dao.FavoriteFilmDao
import com.example.kinopoisk.core.database.entity.FavoriteFilmEntity

@Database(
    entities = [FavoriteFilmEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteFilmDao(): FavoriteFilmDao
}
