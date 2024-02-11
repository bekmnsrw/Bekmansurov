package com.example.kinopoisk.feature.search.data

import com.example.kinopoisk.core.network.KinopoiskApi
import com.example.kinopoisk.feature.search.data.mapper.toSearchResultList
import com.example.kinopoisk.feature.search.domain.SearchRepository
import com.example.kinopoisk.feature.search.domain.dto.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val kinopoiskApi: KinopoiskApi
) : SearchRepository {

    override suspend fun searchFilm(query: String): Flow<List<SearchResult>> = flow {
        emit(
            kinopoiskApi.searchFilm(query = query)
                .films
                .toSearchResultList()
        )
    }
}
