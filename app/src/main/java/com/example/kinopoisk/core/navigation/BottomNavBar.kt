package com.example.kinopoisk.core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.core.widget.KinopoiskButton

@Composable
fun BottomNavBar(
    navController: NavController,
    bottomAppBarItems: List<BottomAppBarItem>
) {
    val startDestination = BottomAppBarItem.Popular
    var selectedTab by remember { mutableIntStateOf(bottomAppBarItems.indexOf(startDestination)) }

    BottomAppBar(
        containerColor = Color.Transparent,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                bottomAppBarItems.forEachIndexed { index, bottomAppBarItem ->
                    val isSelected = index == selectedTab

                    KinopoiskButton(
                        modifier = Modifier.weight(1f),
                        text = bottomAppBarItem.text,
                        containerColor = when (isSelected) {
                            true -> KinopoiskTheme.kinopoiskColor.primary
                            false -> KinopoiskTheme.kinopoiskColor.secondary
                        },
                        contentColor = when (isSelected) {
                            true -> KinopoiskTheme.kinopoiskColor.onPrimary
                            false -> KinopoiskTheme.kinopoiskColor.onSecondary
                        }
                    ) {
                    navController.navigate(bottomAppBarItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                        selectedTab = index
                    }
                }
            }
        }
    )
}
