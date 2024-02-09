package com.example.kinopoisk.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.kinopoisk.core.navigation.navgraph.NavigationGraph
import com.example.kinopoisk.core.navigation.navgraph.popularNavGraph

@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    Scaffold { contentPadding ->
        NavHost(
            modifier = Modifier.padding(contentPadding),
            navController = navController,
            startDestination = NavigationGraph.PopularNavGraph.route
        ) {
            popularNavGraph(navController = navController)
        }
    }
}
