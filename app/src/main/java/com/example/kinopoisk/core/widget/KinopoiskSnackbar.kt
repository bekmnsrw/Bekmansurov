package com.example.kinopoisk.core.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme

@Composable
fun KinopoiskSnackbar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit = {}
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier.padding(bottom = 64.dp),
        snackbar = { snackbarData ->
            Snackbar(
                content = {
                    Text(
                        text = snackbarData.visuals.message,
                        style = KinopoiskTheme.kinopoiskTypography.errorText
                    )
                },
                action = {
                    snackbarData.visuals.actionLabel?.let { actionLabel ->
                        KinopoiskTextButton(
                            text = actionLabel,
                            onClick = onDismiss
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    )
}
