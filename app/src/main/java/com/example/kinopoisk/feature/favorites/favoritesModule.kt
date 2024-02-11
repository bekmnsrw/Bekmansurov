package com.example.kinopoisk.feature.favorites

import com.example.kinopoisk.core.database.AppDatabase
import com.example.kinopoisk.feature.favorites.data.FavoriteRepositoryImpl
import com.example.kinopoisk.feature.favorites.domain.FavoritesRepository
import com.example.kinopoisk.feature.favorites.domain.usecase.DeleteFavoriteFilmByIdUseCase
import com.example.kinopoisk.feature.favorites.domain.usecase.GetAllFavoriteFilmsUseCase
import com.example.kinopoisk.feature.favorites.domain.usecase.GetFavoriteFilmByIdUseCase
import com.example.kinopoisk.feature.favorites.domain.usecase.SaveFavoriteFilmUseCase
import com.example.kinopoisk.feature.favorites.domain.usecase.SearchFavoriteFilmsUseCase
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesModule = module {
    single<FavoritesRepository> {
        provideFavoritesRepository(appDatabase = get())
    }

    viewModel {
        provideFavoritesViewModel(
            deleteFavoriteFilmByIdUseCase = get(),
            getAllFavoriteFilmsUseCase = get()
        )
    }

    factory<GetFavoriteFilmByIdUseCase> {
        provideGetFavoriteFilmByIdUseCase(favoritesRepository = get())
    }

    factory<GetAllFavoriteFilmsUseCase> {
        provideGetAllFavoriteFilmsUseCase(favoritesRepository = get())
    }

    factory<DeleteFavoriteFilmByIdUseCase> {
        provideDeleteFavoriteFilmByIdUseCase(favoritesRepository = get())
    }

    factory<SaveFavoriteFilmUseCase> {
        provideSaveFavoriteFilmUseCase(favoritesRepository = get())
    }

    factory<SearchFavoriteFilmsUseCase> {
        provideSearchFavoriteFilmsUseCase(favoritesRepository = get())
    }
}

private fun provideFavoritesViewModel(
    deleteFavoriteFilmByIdUseCase: DeleteFavoriteFilmByIdUseCase,
    getAllFavoriteFilmsUseCase: GetAllFavoriteFilmsUseCase
): FavoritesViewModel = FavoritesViewModel(
    deleteFavoriteFilmByIdUseCase = deleteFavoriteFilmByIdUseCase,
    getAllFavoriteFilmsUseCase = getAllFavoriteFilmsUseCase
)

private fun provideFavoritesRepository(
    appDatabase: AppDatabase
): FavoritesRepository = FavoriteRepositoryImpl(
    appDatabase = appDatabase
)

private fun provideGetFavoriteFilmByIdUseCase(
    favoritesRepository: FavoritesRepository
): GetFavoriteFilmByIdUseCase = GetFavoriteFilmByIdUseCase(
    favoritesRepository = favoritesRepository
)

private fun provideGetAllFavoriteFilmsUseCase(
    favoritesRepository: FavoritesRepository
): GetAllFavoriteFilmsUseCase = GetAllFavoriteFilmsUseCase(
    favoritesRepository = favoritesRepository
)

private fun provideDeleteFavoriteFilmByIdUseCase(
    favoritesRepository: FavoritesRepository
): DeleteFavoriteFilmByIdUseCase = DeleteFavoriteFilmByIdUseCase(
    favoritesRepository = favoritesRepository
)

private fun provideSaveFavoriteFilmUseCase(
    favoritesRepository: FavoritesRepository
): SaveFavoriteFilmUseCase = SaveFavoriteFilmUseCase(
    favoritesRepository = favoritesRepository
)

private fun provideSearchFavoriteFilmsUseCase(
    favoritesRepository: FavoritesRepository
): SearchFavoriteFilmsUseCase = SearchFavoriteFilmsUseCase(
    favoritesRepository = favoritesRepository
)
