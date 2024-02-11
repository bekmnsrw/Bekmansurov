package com.example.kinopoisk.core.database

import android.content.Context
import androidx.room.Room
import com.example.kinopoisk.core.database.dao.FavoriteFilmDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val DATABASE_NAME = "KinopoiskDb"

val databaseModule = module {
    single<AppDatabase> {
        provideAppDatabase(context = androidApplication().applicationContext)
    }

    single<FavoriteFilmDao> {
        provideFavoriteFilmDao(appDatabase = get())
    }
}

private fun provideAppDatabase(
    context: Context
): AppDatabase = Room.databaseBuilder(
    context = context,
    klass = AppDatabase::class.java,
    name = DATABASE_NAME
).build()

private fun provideFavoriteFilmDao(
    appDatabase: AppDatabase
): FavoriteFilmDao = appDatabase.favoriteFilmDao()
