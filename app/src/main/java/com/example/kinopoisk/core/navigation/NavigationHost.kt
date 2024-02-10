package com.example.kinopoisk.core.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.kinopoisk.core.navigation.navgraph.NavigationGraph
import com.example.kinopoisk.core.navigation.navgraph.popularNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    val bottomAppBarItems = listOf(
        BottomAppBarItem.Popular,
        BottomAppBarItem.Favorites
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                bottomAppBarItems = bottomAppBarItems
            )
        }
    ) { contentPadding ->
        NavHost(
//            modifier = Modifier.padding(contentPadding),
            navController = navController,
            startDestination = NavigationGraph.PopularNavGraph.route
        ) {
            popularNavGraph(navController = navController)
        }
    }
}
