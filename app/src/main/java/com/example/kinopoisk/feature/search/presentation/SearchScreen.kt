package com.example.kinopoisk.feature.search.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kinopoisk.R
import com.example.kinopoisk.core.designsystem.icon.KinopoiskIcons
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.core.navigation.NestedScreen
import com.example.kinopoisk.core.widget.KinopoiskButton
import com.example.kinopoisk.core.widget.KinopoiskProgressBar
import com.example.kinopoisk.core.widget.KinopoiskErrorMessage
import com.example.kinopoisk.core.widget.KinopoiskFilmCard
import com.example.kinopoisk.core.widget.KinopoiskIconButton
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenAction
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenAction.NavigateDetails
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenAction.NavigateUp
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnActiveChange
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnArrowBackClick
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnQueryChange
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenEvent.OnSearchResultClick
import com.example.kinopoisk.feature.search.presentation.SearchViewModel.SearchScreenState
import com.example.kinopoisk.utils.ErrorType
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel = koinViewModel<SearchViewModel>()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
    val searchInput by viewModel.searchInput.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle(initialValue = persistentListOf())

    SearchScreenContent(
        screenState = screenState,
        searchInput = searchInput,
        searchResult = searchResult,
        eventHandler = viewModel::eventHandler
    )

    SearchScreenActions(
        navController = navController,
        screenAction = screenAction
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun SearchScreenContent(
    screenState: SearchScreenState,
    searchInput: String,
    searchResult: List<FilmBrief>,
    eventHandler: (SearchScreenEvent) -> Unit
) {
    println(searchResult.size)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold { _ ->
        FilmSearchBar(
            errorType = screenState.error,
            isLoading = screenState.isLoading,
            searchResult = searchResult,
            searchInput = searchInput,
            onQueryChange = { eventHandler(OnQueryChange(query = it)) },
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus(true)
            },
            active = screenState.shouldShowSearch,
            onActiveChange = { eventHandler(OnActiveChange(isActive = it)) },
            isSearchSuccessful = screenState.isSearchSuccessful,
            onArrowBackClick = { eventHandler(OnArrowBackClick) },
            onSearchResultClick = { eventHandler(OnSearchResultClick(filmId = it)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilmSearchBar(
    errorType: ErrorType?,
    isLoading: Boolean,
    searchResult: List<FilmBrief>,
    searchInput: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    isSearchSuccessful: Boolean,
    onArrowBackClick: () -> Unit,
    onSearchResultClick: (Int) -> Unit
) {
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = searchInput,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                style = KinopoiskTheme.kinopoiskTypography.searchPlaceholder,
                color = KinopoiskTheme.kinopoiskColor.secondaryText
            )
        },
        leadingIcon = {
            KinopoiskIconButton(
                icon = KinopoiskIcons.ArrowBack,
                action = onArrowBackClick
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = KinopoiskTheme.kinopoiskColor.background,
            dividerColor = KinopoiskTheme.kinopoiskColor.background
        )
    ) {
        if (isLoading) {
            KinopoiskProgressBar(shouldShow = true)
        } else {
            when {
                errorType != null -> KinopoiskErrorMessage(
                    errorType = errorType,
                    onClick = {}
                )

                isSearchSuccessful -> SearchResultList(
                    searchResultList = searchResult,
                    onClick = onSearchResultClick
                )

                !isSearchSuccessful -> EmptySearchResult(onClick = onArrowBackClick)
            }
        }
    }
}

@Composable
private fun EmptySearchResult(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        KinopoiskButton(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.not_found),
            containerColor = KinopoiskTheme.kinopoiskColor.primary,
            contentColor = KinopoiskTheme.kinopoiskColor.onPrimary,
            onClick = onClick
        )
    }
}

@Composable
private fun SearchResultList(
    searchResultList: List<FilmBrief>,
    onClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        items(
            items = searchResultList,
            key = { it.filmId },
            contentType = { "SearchResult" }
        ) { filmBrief ->
            KinopoiskFilmCard(
                filmBrief = filmBrief,
                onClick = { onClick(filmBrief.filmId) },
                onLongPress = {}
            )
        }
    }
}

@Composable
private fun SearchScreenActions(
    navController: NavController,
    screenAction: SearchScreenAction?
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateDetails -> navController.navigate(
                NestedScreen.FilmDetails.fromSearchScreen(
                    filmId = screenAction.filmId
                )
            )

            NavigateUp -> navController.navigateUp()
        }
    }
}
