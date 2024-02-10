package com.example.kinopoisk.feature.popular.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kinopoisk.core.navigation.navgraph.NavigationGraph
import com.example.kinopoisk.core.widget.KinopoiskCircularBar
import com.example.kinopoisk.core.widget.KinopoiskFilmCard
import com.example.kinopoisk.core.widget.KinopoiskTopBar
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenAction.*
import com.example.kinopoisk.feature.popular.presentation.PopularViewModel.PopularScreenState
import kotlinx.collections.immutable.PersistentList
import org.koin.androidx.compose.koinViewModel

@Composable
fun PopularScreen(
    navController: NavController,
    viewModel: PopularViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

    PopularScreenContent(
        screenState = screenState
    )

    PopularScreenActions(
        screenAction = screenAction
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopularScreenContent(
    screenState: PopularScreenState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            KinopoiskTopBar(
                scrollBehavior = scrollBehavior,
                title = NavigationGraph.PopularNavGraph.name
            ) {
                // On Search Icon Click
            }
        }
    ) { contentPadding ->
        when (screenState.isLoading) {
            true -> KinopoiskCircularBar(shouldShow = true)
            false -> Top100FilmsList(
                contentPadding = contentPadding,
                top100Films = screenState.top100Films
            )
        }
    }
}

@Composable
private fun Top100FilmsList(
    contentPadding: PaddingValues,
    top100Films: PersistentList<FilmBrief>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        items(
            items = top100Films,
            key = { it.filmId },
            contentType = { "Top100Films" }
        ) {
            KinopoiskFilmCard(
                filmBrief = it,
                onClick = {},
                onLongPress = {}
            )
        }
    }
}

@Composable
private fun PopularScreenActions(
    screenAction: PopularScreenAction?
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit
            is ShowSnackbar -> {}
        }
    }
}

