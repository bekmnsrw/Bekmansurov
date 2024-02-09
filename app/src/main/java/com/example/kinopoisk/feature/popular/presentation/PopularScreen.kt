package com.example.kinopoisk.feature.popular.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun PopularScreen(
    navController: NavController,
    viewModel: PopularViewModel = koinViewModel()
) {
    Column {
        Button(onClick = { viewModel.getTop100Films() }) {
            Text(text = "GET")
        }
    }
}
