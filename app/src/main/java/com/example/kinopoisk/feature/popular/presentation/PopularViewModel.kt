package com.example.kinopoisk.feature.popular.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.feature.popular.domain.usecase.GetTop100FilmsUseCase
import kotlinx.coroutines.launch

class PopularViewModel(
    private val getTop100FilmsUseCase: GetTop100FilmsUseCase
) : ViewModel() {

    fun getTop100Films() = viewModelScope.launch {
        getTop100FilmsUseCase()
            .collect { film ->
                film.forEach {
                    println(it.nameRu)
                }
            }
    }
}
