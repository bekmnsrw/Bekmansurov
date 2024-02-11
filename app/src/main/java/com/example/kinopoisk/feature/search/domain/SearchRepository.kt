package com.example.kinopoisk.feature.search.domain

import com.example.kinopoisk.feature.search.domain.dto.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchFilm(query: String): Flow<List<SearchResult>>
}
