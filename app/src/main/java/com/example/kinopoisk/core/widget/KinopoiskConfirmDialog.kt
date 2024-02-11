package com.example.kinopoisk.core.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.kinopoisk.R
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme

@Composable
fun KinopoiskConfirmDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            KinopoiskTextButton(
                text = stringResource(id = R.string.yes),
                onClick = onConfirmButtonClick
            )
        },
        dismissButton = {
            KinopoiskTextButton(
                text = stringResource(id = R.string.no),
                onClick = onDismissRequest
            )
        },
        title = {
            Text(
                text = title,
                style = KinopoiskTheme.kinopoiskTypography.cardTitle,
                color = KinopoiskTheme.kinopoiskColor.primaryText,
                textAlign = TextAlign.Center
            )
        },
        containerColor = KinopoiskTheme.kinopoiskColor.background
    )
}
