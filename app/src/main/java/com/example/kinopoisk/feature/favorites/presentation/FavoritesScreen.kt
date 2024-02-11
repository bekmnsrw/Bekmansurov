package com.example.kinopoisk.feature.favorites.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.core.navigation.NavigationSource
import com.example.kinopoisk.core.navigation.NestedScreen
import com.example.kinopoisk.core.navigation.navgraph.NavigationGraph
import com.example.kinopoisk.core.widget.KinopoiskConfirmDialog
import com.example.kinopoisk.core.widget.KinopoiskErrorMessage
import com.example.kinopoisk.core.widget.KinopoiskFilmCard
import com.example.kinopoisk.core.widget.KinopoiskProgressBar
import com.example.kinopoisk.core.widget.KinopoiskSnackbar
import com.example.kinopoisk.core.widget.KinopoiskTopBar
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction.NavigateDetails
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction.NavigateSearchScreen
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenAction.ShowSnackbar
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnConfirmDialog
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnDialogDismissRequest
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnFilmCardClick
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnFilmCardPress
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnRetryButtonClick
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenEvent.OnSearchIconClick
import com.example.kinopoisk.feature.favorites.presentation.FavoritesViewModel.FavoritesScreenState
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.utils.ErrorType
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(navController: NavController) {
    val viewModel = koinViewModel<FavoritesViewModel>()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
    val snackbarHostState = remember { SnackbarHostState() }

    FavoritesScreenContent(
        screenState = screenState,
        eventHandler = viewModel::eventHandler,
        snackbarHostState = snackbarHostState
    )

    FavoritesScreenActions(
        navController = navController,
        screenAction = screenAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreenContent(
    screenState: FavoritesScreenState,
    eventHandler: (FavoritesScreenEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scrollBehavior = when (screenState.error) {
        null -> TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
        else -> TopAppBarDefaults.pinnedScrollBehavior(state = rememberTopAppBarState())
    }

    Scaffold(
        snackbarHost = {
            KinopoiskSnackbar(snackbarHostState = snackbarHostState)
        },
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            KinopoiskTopBar(
                scrollBehavior = scrollBehavior,
                title = NavigationGraph.FavoritesNavGraph.name,
                onClick = { eventHandler(OnSearchIconClick) }
            )
        }
    ) { contentPadding ->
        when (screenState.isLoading) {
            true -> KinopoiskProgressBar(shouldShow = true)
            false -> {
                if (screenState.favoriteFilms.isEmpty()) {
                    KinopoiskErrorMessage(errorType = ErrorType.NO_DATA_IN_DB)
                } else {
                    FavoriteFilms(
                        contentPadding = contentPadding,
                        favoriteFilms = screenState.favoriteFilms,
                        onClick = { eventHandler(OnFilmCardClick(kinopoiskId = it)) },
                        onPress = { eventHandler(OnFilmCardPress(filmBrief = it)) }
                    )
                }
            }
        }

        if (screenState.error != null) {
            KinopoiskErrorMessage(errorType = screenState.error) {
                eventHandler(OnRetryButtonClick)
            }
        }

        if (screenState.shouldShowDialog) {
            screenState.selectedFilm?.let { selectedFilm ->
                KinopoiskConfirmDialog(
                    title = "Удалить \"${selectedFilm.nameRu}\" из избранного?",
                    onDismissRequest = { eventHandler(OnDialogDismissRequest) },
                    onConfirmButtonClick = { eventHandler(OnConfirmDialog(selectedFilm)) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteFilms(
    contentPadding: PaddingValues,
    favoriteFilms: PersistentList<FilmBrief>,
    onClick: (Int) -> Unit,
    onPress: (FilmBrief) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(KinopoiskTheme.kinopoiskColor.background)
    ) {
        items(
            items = favoriteFilms,
            key = { it.filmId },
            contentType = { "Top100Films" }
        ) { filmBrief ->
            KinopoiskFilmCard(
                modifier = Modifier.animateItemPlacement(),
                isFavorite = true,
                filmBrief = filmBrief,
                onClick = { onClick(filmBrief.filmId) },
                onPress = { onPress(filmBrief) }
            )
        }
    }
}

@Composable
private fun FavoritesScreenActions(
    navController: NavController,
    screenAction: FavoritesScreenAction?,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateDetails -> navController.navigate(
                NestedScreen.FilmDetails.fromFavoritesScreen(
                    filmId = screenAction.kinopoiskId,
                    source = NavigationSource.FAVORITES.source
                )
            )

            is ShowSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = screenAction.message,
                    duration = SnackbarDuration.Short
                )
            }

            NavigateSearchScreen -> navController.navigate(
                NestedScreen.SearchScreen.fromFavoritesScreen(
                    source = NavigationSource.FAVORITES.source
                )
            )
        }
    }
}
