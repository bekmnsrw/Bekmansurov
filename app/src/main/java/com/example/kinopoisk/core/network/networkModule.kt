package com.example.kinopoisk.core.network

import com.example.kinopoisk.core.network.Qualifier.API_KEY_INTERCEPTOR
import com.example.kinopoisk.core.network.Qualifier.LOGGING_INTERCEPTOR
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 30L
private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"
private const val CONTENT_TYPE = "application/json"

val networkModule = module {
    single<OkHttpClient> {
        provideOkHttpclient(
            loggingInterceptor = get(qualifier = named(LOGGING_INTERCEPTOR)),
            apiKeyInterceptor = get(qualifier = named(API_KEY_INTERCEPTOR))
        )
    }

    single<Retrofit> {
        provideRetrofit(
            okHttpClient = get(),
            converterFactory = get()
        )
    }

    factory<Interceptor>(qualifier = named(LOGGING_INTERCEPTOR)) {
        provideLoggingInterceptor()
    }

    factory<Interceptor>(qualifier = named(API_KEY_INTERCEPTOR)) {
        provideApiKeyInterceptor()
    }

    single<Converter.Factory> {
        provideConverterFactory()
    }

    factory<KinopoiskApi> {
        provideKinopoiskApi(retrofit = get())
    }
}

private fun provideOkHttpclient(
    loggingInterceptor: Interceptor,
    apiKeyInterceptor: Interceptor
): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(apiKeyInterceptor)
    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory,
): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(converterFactory)
    .build()

private fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val json = Json { ignoreUnknownKeys = true }

private fun provideConverterFactory(): Converter.Factory = json.asConverterFactory(
    contentType = CONTENT_TYPE.toMediaType()
)

private fun provideApiKeyInterceptor(): Interceptor = ApiKeyInterceptor()

private fun provideKinopoiskApi(
    retrofit: Retrofit
): KinopoiskApi = retrofit.create(KinopoiskApi::class.java)
