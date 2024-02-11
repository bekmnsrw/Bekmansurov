package com.example.kinopoisk.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_film")
data class FavoriteFilmEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "kinopoisk_id") val kinopoiskId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "poster_url") val posterUrl: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "genre") val genre: String
)
