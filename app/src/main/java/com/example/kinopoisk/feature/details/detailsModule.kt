package com.example.kinopoisk.feature.details

import androidx.lifecycle.SavedStateHandle
import com.example.kinopoisk.core.network.KinopoiskApi
import com.example.kinopoisk.feature.details.data.DetailsRepositoryImpl
import com.example.kinopoisk.feature.details.domain.DetailsRepository
import com.example.kinopoisk.feature.details.domain.usecase.GetFilmDetailsUseCase
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    single<DetailsRepository> {
        provideDetailsRepository(kinopoiskApi = get())
    }

    viewModel {
        provideDetailsViewModel(
            savedStateHandle = get(),
            getFilmDetailsUseCase = get()
        )
    }

    factory<GetFilmDetailsUseCase> {
        provideGetFilmDetailsUseCase(detailsRepository = get())
    }
}

private fun provideDetailsRepository(
    kinopoiskApi: KinopoiskApi
): DetailsRepository = DetailsRepositoryImpl(
    kinopoiskApi = kinopoiskApi
)

private fun provideGetFilmDetailsUseCase(
    detailsRepository: DetailsRepository
): GetFilmDetailsUseCase = GetFilmDetailsUseCase(
    detailsRepository = detailsRepository
)

private fun provideDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    getFilmDetailsUseCase: GetFilmDetailsUseCase
): DetailsViewModel = DetailsViewModel(
    savedStateHandle = savedStateHandle,
    getFilmDetailsUseCase = getFilmDetailsUseCase
)
