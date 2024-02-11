package com.example.kinopoisk.feature.popular.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.popular.domain.usecase.GetTop100FilmsUseCase
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.*
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.*
import com.example.kinopoisk.utils.ErrorType
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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

class PopularViewModel(
    private val getTop100FilmsUseCase: GetTop100FilmsUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(PopularScreenState())
    val screenState: StateFlow<PopularScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<PopularScreenAction?>()
    val screenAction: SharedFlow<PopularScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    data class PopularScreenState(
        val isLoading: Boolean = false,
        val top100Films: PersistentList<FilmBrief> = persistentListOf(),
        val error: ErrorType? = null
    )

    @Immutable
    sealed interface PopularScreenEvent {
        data object OnInit : PopularScreenEvent
        data class OnFilmCardClick(val filmId: Int) : PopularScreenEvent
        data object OnRetryButtonClick : PopularScreenEvent
        data object OnSearchIconClick : PopularScreenEvent
    }

    @Immutable
    sealed interface PopularScreenAction {
        data class NavigateDetails(val filmId: Int) : PopularScreenAction
        data object NavigateSearchScreen : PopularScreenAction
    }

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: PopularScreenEvent) {
        when (event) {
            OnInit -> onInit()
            is OnFilmCardClick -> onFilmCardClick(event.filmId)
            OnRetryButtonClick -> onRetryButtonClick()
            OnSearchIconClick -> onSearchIconClick()
        }
    }

    private fun onInit() = viewModelScope.launch {
        getTop100FilmsUseCase()
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
                            error = ErrorType.NO_INTERNET_CONNECTION
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
            .collect { top100Films ->
                _screenState.emit(
                    _screenState.value.copy(
                        top100Films = top100Films.toPersistentList(),
                        error = null
                    )
                )
            }
    }

    private fun onFilmCardClick(filmId: Int) = viewModelScope.launch {
        _screenAction.emit(
            NavigateDetails(filmId = filmId)
        )
    }

    private fun onRetryButtonClick() = viewModelScope.launch {
        getTop100FilmsUseCase()
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
                            error = ErrorType.NO_INTERNET_CONNECTION
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
            .collect { top100Films ->
                _screenState.emit(
                    _screenState.value.copy(
                        top100Films = top100Films.toPersistentList(),
                        error = null
                    )
                )
            }
    }

    private fun onSearchIconClick() = viewModelScope.launch {
        _screenAction.emit(
            NavigateSearchScreen
        )
    }
}
