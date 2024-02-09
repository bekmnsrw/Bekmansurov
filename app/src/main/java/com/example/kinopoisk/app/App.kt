package com.example.kinopoisk.app

import android.app.Application
import com.example.kinopoisk.core.network.networkModule
import com.example.kinopoisk.feature.popular.popularModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                networkModule,
                popularModule
            )
        }
    }
}
