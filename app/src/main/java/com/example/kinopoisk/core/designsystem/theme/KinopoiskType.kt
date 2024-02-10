package com.example.kinopoisk.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

val LocalTypography = staticCompositionLocalOf<KinopoiskTypography> {
    error("No typography provided")
}

data class KinopoiskTypography(
    val screenHeading: TextStyle,
    val buttonText: TextStyle,
    val cardTitle: TextStyle,
    val cardSupportingText: TextStyle,
    val filmTitle: TextStyle,
    val filmDescriptionKey: TextStyle,
    val filmDescriptionValue: TextStyle,
    val errorText: TextStyle
)

@Composable
fun provideKinopoiskTypography(): KinopoiskTypography = KinopoiskTypography(
    screenHeading = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = TextUnit(value = 25F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    buttonText = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = TextUnit(value = 14F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    cardTitle = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = TextUnit(value = 16F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    cardSupportingText = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = TextUnit(value = 14F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    filmTitle = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = TextUnit(value = 20F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    filmDescriptionKey = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = TextUnit(value = 14F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    filmDescriptionValue = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = TextUnit(value = 14F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    ),
    errorText = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = TextUnit(value = 14F, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16F, type = TextUnitType.Sp)
    )
)
