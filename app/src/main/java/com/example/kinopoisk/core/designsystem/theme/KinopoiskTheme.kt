package com.example.kinopoisk.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object KinopoiskTheme {
    val kinopoiskTypography: KinopoiskTypography
        @Composable get() = LocalTypography.current

    val kinopoiskColor: KinopoiskColor
        @Composable get() = LocalColor.current
}

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    background = BackgroundLight,
    surface = SurfaceLight
)

@Composable
private fun SystemUiColors(
    systemUiController: SystemUiController,
    colorScheme: ColorScheme,
    darkTheme: Boolean
) {
    SideEffect {
        systemUiController.setStatusBarColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
    }
}

@Composable
fun KinopoiskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val kinopoiskTypography = provideKinopoiskTypography()
    val kinopoiskColor = provideKinopoiskColor(darkTheme)

    CompositionLocalProvider(
        values = arrayOf(
            LocalTypography provides kinopoiskTypography,
            LocalColor provides kinopoiskColor
        ),
        content = content
    )

    SystemUiColors(
        systemUiController = rememberSystemUiController(),
        colorScheme = colorScheme,
        darkTheme = darkTheme
    )
}
