package com.example.kinopoisk.feature.search.domain.usecase

import com.example.kinopoisk.feature.search.domain.SearchRepository
import com.example.kinopoisk.feature.search.domain.dto.SearchResult
import kotlinx.coroutines.flow.Flow

class SearchFilmUseCase(
    private val searchRepository: SearchRepository
) {

    suspend operator fun invoke(
        query: String
    ): Flow<List<SearchResult>> = searchRepository.searchFilm(query = query)
}
