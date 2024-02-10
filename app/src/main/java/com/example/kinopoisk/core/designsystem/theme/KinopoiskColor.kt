package com.example.kinopoisk.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Blue = Color(0xFF0094FF)
val LightBlue = Color(0xFFDEEFFF)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Grey = Color(0x99000000)

data class KinopoiskColor(
    val blue: Color,
    val lightBlue: Color,
    val white: Color,
    val black: Color,
    val grey: Color
)

val LocalColor = staticCompositionLocalOf<KinopoiskColor> {
    error("No color provided")
}

@Composable
fun provideKinopoiskColor() = KinopoiskColor(
    blue = Blue,
    lightBlue = LightBlue,
    white = White,
    black = Black,
    grey = Grey
)
