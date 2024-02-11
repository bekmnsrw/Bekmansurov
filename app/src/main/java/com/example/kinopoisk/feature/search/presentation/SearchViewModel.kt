package com.example.kinopoisk.feature.search.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.core.database.mapper.toFilmBriefList
import com.example.kinopoisk.core.navigation.NavArgs
import com.example.kinopoisk.core.navigation.NavigationSource
import com.example.kinopoisk.feature.favorites.domain.usecase.SearchFavoriteFilmsUseCase
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.search.data.mapper.toFilmBriefList
import com.example.kinopoisk.feature.search.domain.usecase.SearchFilmUseCase
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenAction.NavigateDetails
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenAction.NavigateUp
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnActiveChange
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnArrowBackClick
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnQueryChange
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnSearchResultClick
import com.example.kinopoisk.utils.ErrorType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val searchFilmUseCase: SearchFilmUseCase,
    private val searchFavoriteFilmsUseCase: SearchFavoriteFilmsUseCase
) : ViewModel() {

    private val source: String = checkNotNull(savedStateHandle[NavArgs.SOURCE.value]).toString()

    private val _screenState = MutableStateFlow(SearchScreenState())
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<SearchScreenAction?>()
    val screenAction: SharedFlow<SearchScreenAction?> = _screenAction.asSharedFlow()

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput.asStateFlow()

    private suspend fun searchFromRemoteSource(
        query: String
    ): StateFlow<List<FilmBrief>> = searchFilmUseCase(query = query)
        .flowOn(Dispatchers.IO)
        .onStart {
            _screenState.emit(
                _screenState.value.copy(
                    isLoading = true,
                    error = null
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
        .map { it.toFilmBriefList() }
        .onEach {
            _screenState.emit(
                _screenState.value.copy(
                    isSearchSuccessful = it.isNotEmpty()
                )
            )
        }
        .stateIn(viewModelScope)

    private suspend fun searchFromLocalSource(
        query: String
    ): StateFlow<List<FilmBrief>> = searchFavoriteFilmsUseCase(query = query)
        .flowOn(Dispatchers.IO)
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
        .map { it.toFilmBriefList() }
        .onEach {
            _screenState.emit(
                _screenState.value.copy(
                    isSearchSuccessful = it.isNotEmpty()
                )
            )
        }
        .stateIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult: Flow<List<FilmBrief>> = _searchInput
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> flowOf(persistentListOf())
                source == NavigationSource.FAVORITES.source -> searchFromLocalSource(query = query)
                else -> searchFromRemoteSource(query = query)
            }
        }

    @Immutable
    data class SearchScreenState(
        val shouldShowSearch: Boolean = false,
        val isLoading: Boolean = false,
        val error: ErrorType? = null,
        val isSearchSuccessful: Boolean = false
    )

    @Immutable
    sealed interface SearchScreenEvent {
        data object OnArrowBackClick : SearchScreenEvent
        data class OnQueryChange(val query: String) : SearchScreenEvent
        data class OnSearchResultClick(val filmId: Int) : SearchScreenEvent
        data class OnActiveChange(val isActive: Boolean) : SearchScreenEvent
    }

    @Immutable
    sealed interface SearchScreenAction {
        data object NavigateUp : SearchScreenAction
        data class NavigateDetails(val filmId: Int) : SearchScreenAction
    }

    fun eventHandler(event: SearchScreenEvent) {
        when (event) {
            OnArrowBackClick -> onArrowBackClick()
            is OnQueryChange -> onQueryChange(event.query)
            is OnSearchResultClick -> onSearchResultClick(event.filmId)
            is OnActiveChange -> onActiveChange(event.isActive)
        }
    }

    private fun onArrowBackClick() = viewModelScope.launch {
        _screenAction.emit(
            NavigateUp
        )
    }

    private fun onQueryChange(query: String) = viewModelScope.launch {
        _searchInput.value = query
    }

    private fun onSearchResultClick(filmId: Int) = viewModelScope.launch {
        _screenAction.emit(
            NavigateDetails(filmId = filmId)
        )
    }

    private fun onActiveChange(isActive: Boolean) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowSearch = isActive
            )
        )
    }
}
