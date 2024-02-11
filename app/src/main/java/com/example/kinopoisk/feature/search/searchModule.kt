package com.example.kinopoisk.feature.search

import com.example.kinopoisk.core.network.KinopoiskApi
import com.example.kinopoisk.feature.search.data.SearchRepositoryImpl
import com.example.kinopoisk.feature.search.domain.SearchRepository
import com.example.kinopoisk.feature.search.domain.usecase.SearchFilmUseCase
import com.example.kinopoisk.feature.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    single<SearchRepository> {
        provideSearchRepository(kinopoiskApi = get())
    }

    viewModel {
        provideSearchViewModel(searchFilmUseCase = get())
    }

    factory<SearchFilmUseCase> {
        provideSearchFilmUseCase(searchRepository = get())
    }
}

private fun provideSearchRepository(
    kinopoiskApi: KinopoiskApi
): SearchRepository = SearchRepositoryImpl(
    kinopoiskApi = kinopoiskApi
)

private fun provideSearchFilmUseCase(
    searchRepository: SearchRepository
): SearchFilmUseCase = SearchFilmUseCase(
    searchRepository = searchRepository
)

private fun provideSearchViewModel(
    searchFilmUseCase: SearchFilmUseCase
): SearchViewModel = SearchViewModel(
    searchFilmUseCase = searchFilmUseCase
)
