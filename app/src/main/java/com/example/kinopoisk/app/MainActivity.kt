package com.example.kinopoisk.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.kinopoisk.core.designsystem.theme.KinopoiskTheme
import com.example.kinopoisk.core.navigation.NavigationHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KinopoiskTheme {
                CompositionLocalProvider {
                    NavigationHost()
                }
            }
        }
    }
}
