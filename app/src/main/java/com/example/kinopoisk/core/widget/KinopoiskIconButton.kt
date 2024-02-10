package com.example.kinopoisk.core.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme

@Composable
fun KinopoiskIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    action: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = action
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = KinopoiskTheme.kinopoiskColor.blue
        )
    }
}
