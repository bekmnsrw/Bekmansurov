package com.example.kinopoisk.core.widget

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme

@Composable
fun KinopoiskTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = KinopoiskTheme.kinopoiskColor.background,
            contentColor = KinopoiskTheme.kinopoiskColor.primary
        )
    ) {
        Text(
            text = text,
            style = KinopoiskTheme.kinopoiskTypography.buttonText
        )
    }
}
