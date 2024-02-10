package com.example.kinopoisk.feature.details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kinopoisk.R
import com.example.kinopoisk.core.designsystem.icon.KinopoiskIcons
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.core.widget.KinopoiskCircularBar
import com.example.kinopoisk.core.widget.KinopoiskIconButton
import com.example.kinopoisk.core.widget.KinopoiskImage
import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenAction
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenAction.NavigateUp
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenEvent
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenEvent.OnArrowBackClick
import com.example.kinopoisk.feature.details.presentation.DetailsViewModel.DetailsScreenState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(navController: NavController) {
    val viewModel = koinViewModel<DetailsViewModel>()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

    DetailsScreenContent(
        screenState = screenState,
        eventHandler = viewModel::eventHandler
    )

    DetailsScreenActions(
        navController = navController,
        screenAction = screenAction
    )
}

@Composable
private fun DetailsScreenContent(
    screenState: DetailsScreenState,
    eventHandler: (DetailsScreenEvent) -> Unit
) {
    Scaffold { contentPadding ->
        when (screenState.isLoading) {
            true -> KinopoiskCircularBar(shouldShow = true)
            false -> {
                FilmDetails(
                    contentPadding = contentPadding,
                    filmDetails = screenState.filmDetails,
                    onArrowBackClick = { eventHandler(OnArrowBackClick) }
                )
            }
        }
    }
}

@Composable
private fun FilmDetails(
    contentPadding: PaddingValues,
    filmDetails: FilmDetails?,
    onArrowBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        item {
            FilmPoster(
                imageUrl = filmDetails?.imageUrl,
                onArrowBackClick = onArrowBackClick
            )
        }
        item {
            FilmInformation(filmDetails = filmDetails)
        }
    }
}

@Composable
private fun FilmPoster(
    imageUrl: String?,
    onArrowBackClick: () -> Unit
) {
    Box {
        imageUrl?.let {
            KinopoiskImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(533.dp),
                imageUrl = imageUrl
            )
        }
        KinopoiskIconButton(
            modifier = Modifier.align(Alignment.TopStart),
            icon = KinopoiskIcons.ArrowBack,
            action = onArrowBackClick
        )
    }
}

@Composable
private fun FilmInformation(filmDetails: FilmDetails?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        filmDetails?.let {
            Text(
                text = it.nameRu,
                style = KinopoiskTheme.kinopoiskTypography.filmTitle,
                color = KinopoiskTheme.kinopoiskColor.primaryText
            )
            Text(
                text = it.description,
                style = KinopoiskTheme.kinopoiskTypography.filmDescriptionValue,
                color = KinopoiskTheme.kinopoiskColor.secondaryText
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FilmGenres(genres = it.genres)
                FilmCountries(countries = it.countries)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilmGenres(genres: String) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(id = R.string.genres),
            style = KinopoiskTheme.kinopoiskTypography.filmDescriptionKey,
            color = KinopoiskTheme.kinopoiskColor.secondaryText
        )
        Text(
            text = genres,
            style = KinopoiskTheme.kinopoiskTypography.filmDescriptionValue,
            color = KinopoiskTheme.kinopoiskColor.secondaryText
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilmCountries(countries: String) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(id = R.string.countries),
            style = KinopoiskTheme.kinopoiskTypography.filmDescriptionKey,
            color = KinopoiskTheme.kinopoiskColor.secondaryText
        )
        Text(
            text = countries,
            style = KinopoiskTheme.kinopoiskTypography.filmDescriptionValue,
            color = KinopoiskTheme.kinopoiskColor.secondaryText
        )
    }
}

@Composable
private fun DetailsScreenActions(
    navController: NavController,
    screenAction: DetailsScreenAction?
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateUp -> navController.navigateUp()
        }
    }
}
