package com.example.kinopoisk.feature.popular.presentation

import android.annotation.SuppressLint
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
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.NavigateDetails
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.NavigateSearchScreen
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.ShowSnackbar
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnConfirmDialog
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnDialogDismissRequest
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnFilmCardClick
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnFilmCardPress
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnRetryButtonClick
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenEvent.OnSearchIconClick
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenState
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun PopularScreen(navController: NavController) {
    val viewModel = koinViewModel<PopularViewModel>()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
    val snackbarHostState = remember { SnackbarHostState() }

    PopularScreenContent(
        screenState = screenState,
        eventHandler = viewModel::eventHandler,
        snackbarHostState = snackbarHostState
    )

    PopularScreenActions(
        screenAction = screenAction,
        navController = navController,
        snackbarHostState = snackbarHostState
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopularScreenContent(
    screenState: PopularScreenState,
    eventHandler: (PopularScreenEvent) -> Unit,
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
                title = NavigationGraph.PopularNavGraph.name,
                onClick = { eventHandler(OnSearchIconClick) }
            )
        }
    ) { contentPadding ->
        when (screenState.isLoading) {
            true -> KinopoiskProgressBar(shouldShow = true)
            false -> Top100FilmsList(
                contentPadding = contentPadding,
                top100Films = screenState.top100Films,
                onClick = { eventHandler(OnFilmCardClick(filmId = it)) },
                onPress = { eventHandler(OnFilmCardPress(filmBrief = it)) }
            )
        }

        if (screenState.error != null) {
            KinopoiskErrorMessage(errorType = screenState.error) {
                eventHandler(OnRetryButtonClick)
            }
        }

        if (screenState.shouldShowDialog) {
            screenState.selectedFilm?.let { selectedFilm ->
                KinopoiskConfirmDialog(
                    title = "Добавить \"${selectedFilm.nameRu}\" в избранное?",
                    onDismissRequest = { eventHandler(OnDialogDismissRequest) },
                    onConfirmButtonClick = { eventHandler(OnConfirmDialog(selectedFilm)) }
                )
            }
        }
    }
}

@Composable
private fun Top100FilmsList(
    contentPadding: PaddingValues,
    top100Films: PersistentList<FilmBrief>,
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
            items = top100Films,
            key = { it.filmId },
            contentType = { "Top100Films" }
        ) { filmBrief ->
            KinopoiskFilmCard(
                isFavorite = false,
                filmBrief = filmBrief,
                onClick = { onClick(filmBrief.filmId) },
                onPress = { onPress(filmBrief) }
            )
        }
    }
}

@Composable
private fun PopularScreenActions(
    screenAction: PopularScreenAction?,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateDetails -> navController.navigate(
                NestedScreen.FilmDetails.fromPopularScreen(
                    filmId = screenAction.filmId,
                    source = NavigationSource.POPULAR.source
                )
            )

            NavigateSearchScreen -> navController.navigate(
                NestedScreen.SearchScreen.fromPopularScreen(
                    source = NavigationSource.POPULAR.source
                )
            )

            is ShowSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = screenAction.message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

