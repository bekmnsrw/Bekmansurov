package com.example.kinopoisk.core.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.feature.popular.domain.dto.FilmBrief

@Composable
fun KinopoiskFilmCard(
    filmBrief: FilmBrief,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(size = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = KinopoiskTheme.kinopoiskColor.white)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KinopoiskImage(
                modifier = Modifier
                    .width(40.dp)
                    .height(63.dp)
                    .clip(RoundedCornerShape(5.dp)),
                imageUrl = filmBrief.posterUrlPreview
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                KinopoiskCardTitle(text = filmBrief.nameRu)
                KinopoiskCardSupportingText(
                    genre = filmBrief.genre,
                    year = filmBrief.year
                )
            }
        }
    }
}

@Composable
private fun KinopoiskCardTitle(text: String) {
    Text(
        text = text,
        style = KinopoiskTheme.kinopoiskTypography.cardTitle,
        color = KinopoiskTheme.kinopoiskColor.black,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun KinopoiskCardSupportingText(
    genre: String,
    year: Int
) {
    Text(
        text = "$genre ($year)",
        style = KinopoiskTheme.kinopoiskTypography.cardSupportingText,
        color = KinopoiskTheme.kinopoiskColor.grey,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
