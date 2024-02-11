package com.example.kinopoisk.feature.favorites.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.core.database.mapper.toFilmBriefList
import com.example.kinopoisk.feature.favorites.domain.usecase.DeleteFavoriteFilmByIdUseCase
import com.example.kinopoisk.feature.favorites.domain.usecase.GetAllFavoriteFilmsUseCase
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction.NavigateDetails
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction.NavigateSearchScreen
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction.ShowSnackbar
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnConfirmDialog
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnDialogDismissRequest
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnFilmCardClick
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnFilmCardPress
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnInit
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnRetryButtonClick
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnSearchIconClick
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
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
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val deleteFavoriteFilmByIdUseCase: DeleteFavoriteFilmByIdUseCase,
    private val getAllFavoriteFilmsUseCase: GetAllFavoriteFilmsUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(FavoritesScreenState())
    val screenState: StateFlow<FavoritesScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<FavoritesScreenAction?>()
    val screenAction: SharedFlow<FavoritesScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    data class FavoritesScreenState(
        val isLoading: Boolean = false,
        val favoriteFilms: PersistentList<FilmBrief> = persistentListOf(),
        val error: ErrorType? = null,
        val shouldShowDialog: Boolean = false,
        val selectedFilm: FilmBrief? = null
    )

    @Immutable
    sealed interface FavoritesScreenEvent {
        data object OnInit : FavoritesScreenEvent
        data class OnFilmCardClick(val kinopoiskId: Int) : FavoritesScreenEvent
        data class OnFilmCardPress(val filmBrief: FilmBrief) : FavoritesScreenEvent
        data object OnSearchIconClick : FavoritesScreenEvent
        data object OnRetryButtonClick : FavoritesScreenEvent
        data object OnDialogDismissRequest : FavoritesScreenEvent
        data class OnConfirmDialog(val filmBrief: FilmBrief) : FavoritesScreenEvent
    }

    @Immutable
    sealed interface FavoritesScreenAction {
        data class NavigateDetails(val kinopoiskId: Int) : FavoritesScreenAction
        data class ShowSnackbar(val message: String) : FavoritesScreenAction
        data object NavigateSearchScreen : FavoritesScreenAction
    }

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: FavoritesScreenEvent) {
        when (event) {
            OnInit -> onInit()
            is OnFilmCardClick -> onFilmCardClick(event.kinopoiskId)
            is OnFilmCardPress -> onFilmCardPress(event.filmBrief)
            OnSearchIconClick -> onSearchIconClick()
            OnRetryButtonClick -> onRetryButtonClick()
            is OnConfirmDialog -> onConfirmDialog(event.filmBrief)
            OnDialogDismissRequest -> onDialogDismissRequest()
        }
    }

    private fun onInit() = viewModelScope.launch {
        getAllFavoriteFilmsUseCase()
            .flowOn(Dispatchers.IO)
            .catch {
                _screenState.emit(
                    _screenState.value.copy(
                        error = ErrorType.OTHER
                    )
                )
                println(it)
            }
            .collect { favoriteFilms ->
                _screenState.emit(
                    _screenState.value.copy(
                        favoriteFilms = favoriteFilms
                            .toFilmBriefList()
                            .toPersistentList(),
                        error = null
                    )
                )
            }
    }

    private fun onFilmCardClick(kinopoiskId: Int) = viewModelScope.launch {
        _screenAction.emit(
            NavigateDetails(kinopoiskId = kinopoiskId)
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

    private fun onSearchIconClick() = viewModelScope.launch {
        _screenAction.emit(
            NavigateSearchScreen
        )
    }

    private fun onRetryButtonClick() = viewModelScope.launch {
        getAllFavoriteFilmsUseCase()
            .flowOn(Dispatchers.IO)
            .catch {
                _screenState.emit(
                    _screenState.value.copy(
                        error = ErrorType.OTHER
                    )
                )
                println(it)
            }
            .collect { favoriteFilms ->
                _screenState.emit(
                    _screenState.value.copy(
                        favoriteFilms = favoriteFilms
                            .toFilmBriefList()
                            .toPersistentList(),
                        error = null
                    )
                )
            }
    }

    private fun onDialogDismissRequest() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
            )
        )
    }

    private fun onConfirmDialog(filmBrief: FilmBrief) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false,
                favoriteFilms = _screenState.value.favoriteFilms.remove(filmBrief)
            )
        )

        deleteFavoriteFilmByIdUseCase(kinopoiskId = filmBrief.filmId)

        _screenAction.emit(
            ShowSnackbar(
                message = "Фильм \"${filmBrief.nameRu}\" удален из избранного"
            )
        )
    }
}
