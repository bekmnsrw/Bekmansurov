package com.example.kinopoisk.core.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    private companion object {
        const val API_KEY_HEADER_NAME = "x-api-key"
        const val API_KEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        return chain.proceed(
            originalRequest.newBuilder()
            .header(name = API_KEY_HEADER_NAME, value = API_KEY)
            .build()
        )
    }
}
