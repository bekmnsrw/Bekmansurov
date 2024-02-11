package com.example.kinopoisk

import com.example.kinopoisk.feature.details.domain.DetailsRepository
import com.example.kinopoisk.feature.details.domain.dto.FilmDetails
import com.example.kinopoisk.feature.details.domain.usecase.GetFilmDetailsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

class GetFilmDetailsUseCaseTest {

    @MockK
    lateinit var detailsRepository: DetailsRepository

    private lateinit var getFilmDetailsUseCase: GetFilmDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFilmDetailsUseCase = GetFilmDetailsUseCase(
            detailsRepository = detailsRepository
        )
    }

    @Test
    fun `When invoked, expect to receive flow of FilmDetails`() {
        /* Arrange */
        val testFilmId = 1

        val expectedResult: Flow<FilmDetails> = flowOf(
            mockk {
                every { filmId } returns 1
                every { nameRu } returns "testName"
                every { description } returns "testDescription"
                every { genres } returns "testGenre"
                every { countries } returns "testCountry"
                every { imageUrl } returns "testImageUrl"
            }
        )

        coEvery {
            detailsRepository.getFilmDetails(filmId = testFilmId)
        } returns expectedResult

        /* Act */
        runTest {
            val result = getFilmDetailsUseCase(
                filmId = testFilmId
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val testFilmId = 1

        coEvery {
            detailsRepository.getFilmDetails(
                filmId = testFilmId
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getFilmDetailsUseCase(
                    filmId = testFilmId
                )
            }
        }
    }
}
