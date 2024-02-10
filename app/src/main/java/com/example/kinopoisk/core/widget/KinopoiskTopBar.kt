package com.example.kinopoisk.core.widget

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kinopoisk.core.designsystem.icon.KinopoiskIcons
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KinopoiskTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onClick: () -> Unit
) = TopAppBar(
    modifier = modifier,
    title = {
        Text(
            text = title,
            style = KinopoiskTheme.kinopoiskTypography.screenHeading,
            color = KinopoiskTheme.kinopoiskColor.black
        )
    },
    scrollBehavior = scrollBehavior,
    actions = {
        KinopoiskIconButton(
            icon = KinopoiskIcons.Search,
            action = onClick
        )
    }
)
