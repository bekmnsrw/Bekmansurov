package com.example.kinopoisk.feature.details.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.feature.details.data.mapper.toFilmDetails
import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import com.example.kinopoisk.feature.details.domain.usecase.GetFilmDetailsUseCase
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenAction.*
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenEvent.*
import com.example.kinopoisk.feature.favorites.domain.usecase.GetFavoriteFilmByIdUseCase
import com.example.kinopoisk.utils.ErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFilmDetailsUseCase: GetFilmDetailsUseCase,
    private val getFavoriteFilmByIdUseCase: GetFavoriteFilmByIdUseCase
) : ViewModel() {

    private companion object {
        const val FILM_ID = "filmId"
        const val SOURCE = "source"
    }

    private val filmId: Int = checkNotNull(savedStateHandle[FILM_ID]).toString().toInt()
    private val source: String = checkNotNull(savedStateHandle[SOURCE]).toString()

    private val _screenState = MutableStateFlow(DetailsScreenState())
    val screenState: StateFlow<DetailsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<DetailsScreenAction?>()
    val screenAction: SharedFlow<DetailsScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    data class DetailsScreenState(
        val isLoading: Boolean = false,
        val filmDetails: FilmDetails? = null,
        val error: ErrorType? = null
    )

    @Immutable
    sealed interface DetailsScreenEvent {
        data object OnInit : DetailsScreenEvent
        data object OnArrowBackClick : DetailsScreenEvent
        data object OnRetryButtonClick : DetailsScreenEvent
    }

    @Immutable
    sealed interface DetailsScreenAction {
        data object NavigateUp : DetailsScreenAction
    }

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: DetailsScreenEvent) {
        when (event) {
            OnInit -> onInit()
            OnArrowBackClick -> onArrowBackClick()
            OnRetryButtonClick -> onRetryButtonClick()
        }
    }

    private suspend fun getFilmDetailsFromRemoteSource(filmId: Int) = viewModelScope.launch {
        getFilmDetailsUseCase(filmId = filmId)
            .flowOn(Dispatchers.IO)
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = false
                    )
                )
            }
            .catch {
                when (it) {
                    is UnknownHostException -> _screenState.emit(
                        _screenState.value.copy(
                            error = ErrorType.UNKNOWN_HOST_EXCEPTION
                        )
                    )
                    else -> _screenState.emit(
                        _screenState.value.copy(
                            error = ErrorType.OTHER
                        )
                    )
                }
                println(it)
            }
            .collect { filmDetails ->
                _screenState.emit(
                    _screenState.value.copy(
                        filmDetails = filmDetails,
                        error = null
                    )
                )
            }
    }

    private suspend fun getFilmDetailsFromLocalSource(filmId: Int) = viewModelScope.launch {
        getFavoriteFilmByIdUseCase(kinopoiskId = filmId)
            .flowOn(Dispatchers.IO)
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = false
                    )
                )
            }
            .catch { println(it) }
            .collect { filmDetails ->
                _screenState.emit(
                    _screenState.value.copy(
                        filmDetails = filmDetails.toFilmDetails()
                    )
                )
            }
    }

    private fun onInit() = viewModelScope.launch {
        when (source) {
            "FAVORITES" -> getFilmDetailsFromLocalSource(filmId = filmId)
            else -> getFilmDetailsFromRemoteSource(filmId = filmId)
        }
    }

    private fun onArrowBackClick() = viewModelScope.launch {
        _screenAction.emit(
            NavigateUp
        )
    }

    private fun onRetryButtonClick() = viewModelScope.launch {
        getFilmDetailsUseCase(filmId = filmId)
            .flowOn(Dispatchers.IO)
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        error = null,
                        isLoading = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = false
                    )
                )
            }
            .catch {
                when (it) {
                    is UnknownHostException -> _screenState.emit(
                        _screenState.value.copy(
                            error = ErrorType.UNKNOWN_HOST_EXCEPTION
                        )
                    )
                    else -> _screenState.emit(
                        _screenState.value.copy(
                            error = ErrorType.OTHER
                        )
                    )
                }
                println(it)
            }
            .collect { filmDetails ->
                _screenState.emit(
                    _screenState.value.copy(
                        filmDetails = filmDetails,
                        error = null
                    )
                )
            }
    }
}
