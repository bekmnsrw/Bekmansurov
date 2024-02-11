package com.example.kinopoisk.feature.popular

import com.example.kinopoisk.core.network.KinopoiskApi
import com.example.kinopoisk.feature.favorites.domain.usecase.GetAllFavoriteFilmsUseCase
import com.example.kinopoisk.feature.favorites.domain.usecase.SaveFavoriteFilmUseCase
import com.example.kinopoisk.feature.popular.data.PopularRepositoryImpl
import com.example.kinopoisk.feature.popular.domain.PopularRepository
import com.example.kinopoisk.feature.popular.domain.usecase.GetTop100FilmsUseCase
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val popularModule = module {
    single<PopularRepository> {
        providePopularRepository(kinopoiskApi = get())
    }

    viewModel {
        providePopularViewModel(
            getTop100FilmsUseCase = get(),
            saveFavoriteFilmUseCase = get(),
            getAllFavoriteFilmsUseCase = get()
        )
    }

    factory<GetTop100FilmsUseCase> {
        provideGetTop100FilmsUseCase(popularRepository = get())
    }
}

private fun providePopularViewModel(
    getTop100FilmsUseCase: GetTop100FilmsUseCase,
    saveFavoriteFilmUseCase: SaveFavoriteFilmUseCase,
    getAllFavoriteFilmsUseCase: GetAllFavoriteFilmsUseCase
): PopularViewModel = PopularViewModel(
    getTop100FilmsUseCase = getTop100FilmsUseCase,
    saveFavoriteFilmUseCase = saveFavoriteFilmUseCase,
    getAllFavoriteFilmsUseCase = getAllFavoriteFilmsUseCase
)

private fun providePopularRepository(
    kinopoiskApi: KinopoiskApi
): PopularRepository = PopularRepositoryImpl(
    kinopoiskApi = kinopoiskApi
)

private fun provideGetTop100FilmsUseCase(
    popularRepository: PopularRepository
): GetTop100FilmsUseCase = GetTop100FilmsUseCase(
    popularRepository = popularRepository
)
