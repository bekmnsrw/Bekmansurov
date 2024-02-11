package com.example.kinopoisk.feature.popular.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.feature.favorites.domain.usecase.SaveFavoriteFilmUseCase
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.popular.domain.usecase.GetTop100FilmsUseCase
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.NavigateDetails
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.NavigateSearchScreen
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.ShowSnackbar
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnConfirmDialog
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnDialogDismissRequest
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnFilmCardClick
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnFilmCardPress
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnInit
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnRetryButtonClick
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnSearchIconClick
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
    private val getTop100FilmsUseCase: GetTop100FilmsUseCase,
    private val saveFavoriteFilmUseCase: SaveFavoriteFilmUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(PopularScreenState())
    val screenState: StateFlow<PopularScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<PopularScreenAction?>()
    val screenAction: SharedFlow<PopularScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    data class PopularScreenState(
        val isLoading: Boolean = false,
        val top100Films: PersistentList<FilmBrief> = persistentListOf(),
        val favoriteFilmsIds: PersistentList<Int> = persistentListOf(),
        val error: ErrorType? = null,
        val shouldShowDialog: Boolean = false,
        val selectedFilm: FilmBrief? = null
    )

    @Immutable
    sealed interface PopularScreenEvent {
        data object OnInit : PopularScreenEvent
        data class OnFilmCardClick(val filmId: Int) : PopularScreenEvent
        data object OnRetryButtonClick : PopularScreenEvent
        data object OnSearchIconClick : PopularScreenEvent
        data class OnFilmCardPress(val filmBrief: FilmBrief) : PopularScreenEvent
        data object OnDialogDismissRequest : PopularScreenEvent
        data class OnConfirmDialog(val filmBrief: FilmBrief) : PopularScreenEvent
    }

    @Immutable
    sealed interface PopularScreenAction {
        data class NavigateDetails(val filmId: Int) : PopularScreenAction
        data object NavigateSearchScreen : PopularScreenAction
        data class ShowSnackbar(val message: String) : PopularScreenAction
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
            is OnFilmCardPress -> onFilmCardPress(event.filmBrief)
            OnDialogDismissRequest -> onDialogDismissRequest()
            is OnConfirmDialog -> onConfirmDialog(event.filmBrief)
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

    private fun onFilmCardPress(filmBrief: FilmBrief) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = true,
                selectedFilm = filmBrief
            )
        )
    }

    private fun onDialogDismissRequest() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
            )
        )
    }

    private fun onConfirmDialog(filmBrief: FilmBrief) = viewModelScope.launch {
        saveFavoriteFilmUseCase(filmBrief = filmBrief)

        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
            )
        )

        _screenAction.emit(
            ShowSnackbar(
                message = "Фильм \"${filmBrief.nameRu}\" добавлен в избранное"
            )
        )
    }
}
