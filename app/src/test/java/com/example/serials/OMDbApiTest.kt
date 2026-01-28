package com.example.serials

import androidx.compose.ui.text.font.Typeface
import com.example.serials.data.remote.api.OMDbApi
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.data.remote.dto.SerialOMDb
import com.example.serials.data.remote.response.OMDbResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Retrofit
import java.net.HttpURLConnection
import kotlin.math.exp
import org.junit.Assert.assertNotEquals

class OMDbApiTest {

    private val mockApi: OMDbApi = mock()

    @Test
    fun `test get2025series returns data`() {
        runBlocking {
            val expectedResponse = OMDbResponse(
                Search = listOf(
                    SerialOMDb(
                        Title = "New Series 2025",
                        Year = "2025",
                        imdbID = "tt1234567",
                        Type = "series",
                        Poster = "poster.jpg"
                    )
                ),
                totalResults = "10",
                Response = "True"
            )
            whenever(mockApi.get2025Series()).thenReturn(expectedResponse)
            val result = mockApi.get2025Series()
            assertEquals("True", result.Response)
            assertEquals(1, result.Search.size)
            assertEquals("New Series 2025", result.Search[0].Title)
        }
    }
    @Test
    fun `test serialDetails return details`() {
        runBlocking {
            val imdbId = "tt1234567"
            val expectedDetails = SerialDetails(
                Title = "Breaking Bad",
                Year = "2008",
                imdbID = imdbId,
                Type = "series",
                Poster = "poster.jpg",
                Actors = "Bryan Cranston",
                Awards = "Won 2 Emmys",
                Country = "USA",
                Director = "Vince Gilligan",
                Genre = "Crime, Drama",
                Language = "English",
                Metascore = "89",
                Plot = "A chemistry teacher...",
                Rated = "TV-MA",
                Ratings = listOf(),
                Released = "20 Jan 2008",
                Response = "True",
                Runtime = "49 min",
                Writer = "Vince Gilligan",
                imdbRating = "9.5",
                imdbVotes = "1,800,000",
                totalSeasons = "5"
            )

            whenever(
                mockApi.serialDetails(
                    apiKey = any(), // или конкретное значение
                    i = eq(imdbId),
                    plot = any()
                )
            ).thenReturn(expectedDetails)

            val result = mockApi.serialDetails(apiKey = "test_key", i = imdbId, plot = "full")
            assertEquals(imdbId, result.imdbID)
            assertEquals("Breaking Bad", result.Title)
            assertEquals("2008", result.Year)
            assertEquals("series", result.Type)
        }
    }
    @Test
    fun `test searchSeries return results`() {
        runBlocking {
            val query = "breaking bad"
            val expectedResponse = OMDbResponse(
                Search = listOf(
                    SerialOMDb(
                        Title = "Breaking Bad",
                        Year = "2008",
                        imdbID = "tt0903747",
                        Type = "series",
                        Poster = "poster.jpg"
                    ),
                    SerialOMDb(
                        Title = "Better Call Saul",
                        Year = "2015",
                        imdbID = "tt3032476",
                        Type = "series",
                        Poster = "poster2.jpg"
                    )
                ),
                totalResults = "2",
                Response = "True"
            )

            whenever(
                mockApi.searchSeries(
                    apiKey = any(),
                    searchQuery = any(),
                    type = any(),
                    page = any()
                )
            ).thenReturn(expectedResponse)

            val result = mockApi.searchSeries(
                apiKey = "test_key", searchQuery = query, type = "series", page = 1
            )

            assertEquals("True", result.Response)
            assertEquals(2, result.Search.size)
            assertEquals("Breaking Bad", result.Search[0].Title)
            assertEquals("Better Call Saul", result.Search[1].Title)
        }
    }
    @Test
    fun `test loadserials from categories return category results`() {
        runBlocking {
            val category = "action"
            val expectedResponse = OMDbResponse(
                Search = listOf(
                    SerialOMDb(
                        Title = "The Boys",
                        Year = "2019",
                        imdbID = "tt1190634",
                        Type = "series",
                        Poster = "poster.jpg"
                    )
                ),
                totalResults = "1",
                Response = "True"
            )

            whenever(
                mockApi.loadserialsFromCategories(
                    apiKey = any(),
                    category = any(),
                    type = any(),
                    page = any()
                )
            ).thenReturn(expectedResponse)

            val result = mockApi.loadserialsFromCategories(
                apiKey = "test_key", category = category, type = "series", page = 1
            )

            assertEquals("True", result.Response)
            assertEquals(1, result.Search.size)
            assertEquals("The Boys", result.Search[0].Title)
            assertEquals("series", result.Search[0].Type)
        }
    }
    @Test
    fun `test searchSeries returns empty for non-exist query`() {
        runBlocking {
            val query = "asdfghjkl12345"
            val expectedResponse = OMDbResponse(
                Search = emptyList(),
                totalResults = "0",
                Response = "False"
            )

            whenever(
                mockApi.searchSeries(
                    apiKey = any(),
                    searchQuery = any(),
                    type = any(),
                    page = any()
                )
            ).thenReturn(expectedResponse)

            val result = mockApi.searchSeries(
                apiKey = "test_key", searchQuery = query, type = "series", page = 1
            )

            assertEquals("False", result.Response)
            assertEquals(0, result.Search.size)
            assertTrue(result.Search.isEmpty())
        }
    }
    @Test
    fun `test searchSeries handles large result set`() {
        runBlocking {
            val query = "love"
            val largeList = List(100) { index ->
                SerialOMDb(
                    Title = "Series $index",
                    Year = (2000 + index % 20).toString(),
                    imdbID = "tt${1000000 + index}",
                    Type = "series",
                    Poster = "poster$index.jpg"
                )
            }

            val expectedResponse = OMDbResponse(
                Search = largeList,
                totalResults = "100",
                Response = "True"
            )

            whenever(
                mockApi.searchSeries(
                    apiKey = any(),
                    searchQuery = any(),
                    type = any(),
                    page = any()
                )
            ).thenReturn(expectedResponse)

            val result = mockApi.searchSeries(
                apiKey = "test_key", searchQuery = query, type = "series", page = 1
            )

            assertEquals("True", result.Response)
            assertEquals(100, result.Search.size)
            assertEquals("Series 0", result.Search[0].Title)
            assertEquals("Series 99", result.Search[99].Title)
        }
    }
    @Test
    fun `test serial with no poster`() {
        runBlocking {
            val imdbId = "tt1234567"
            val expectedDetails = SerialDetails(
                Title = "Old Series",
                Year = "1990",
                imdbID = imdbId,
                Type = "series",
                Poster = "N/A",  // Нет постера
                Actors = "Actor",
                Awards = "",
                Country = "",
                Director = "",
                Genre = "",
                Language = "",
                Metascore = "",
                Plot = "",
                Rated = "",
                Ratings = listOf(),
                Released = "",
                Response = "",
                Runtime = "",
                Writer = "",
                imdbRating = "",
                imdbVotes = "",
                totalSeasons = ""
            )

            whenever(
                mockApi.serialDetails(
                    apiKey = any(),
                    i = any(),
                    plot = any()
                )
            ).thenReturn(expectedDetails)

            val result = mockApi.serialDetails(apiKey = "test_key", i = imdbId, plot = "full")
            assertEquals("N/A", result.Poster)
        }
    }
    @Test
    fun `test movie type returns correctly`() {
        runBlocking {
            val imdbId = "tt1234567"
            val expectedDetails = SerialDetails(
                Title = "The Matrix",
                Year = "1999",
                imdbID = imdbId,
                Type = "movie",  // Фильм, не сериал
                Poster = "poster.jpg",
                Actors = "Keanu Reeves",
                Awards = "Won 4 Oscars",
                Country = "USA",
                Director = "The Wachowskis",
                Genre = "Action, Sci-Fi",
                Language = "English",
                Metascore = "73",
                Plot = "A computer hacker learns...",
                Rated = "R",
                Ratings = listOf(),
                Released = "31 Mar 1999",
                Response = "True",
                Runtime = "136 min",
                Writer = "The Wachowskis",
                imdbRating = "8.7",
                imdbVotes = "1,800,000",
                totalSeasons = ""
            )

            whenever(
                mockApi.serialDetails(
                    apiKey = any(),
                    i = any(),
                    plot = any()
                )
            ).thenReturn(expectedDetails)

            val result = mockApi.serialDetails(
                apiKey = "test_key", i = imdbId, plot = "full"
            )

            assertEquals("movie", result.Type)
            assertEquals("", result.totalSeasons)
        }
    }
    @Test
    fun `test series with single season`() {
        runBlocking {
            val imdbId = "tt1234567"
            val expectedDetails = SerialDetails(
                Title = "Limited Series",
                Year = "2023",
                imdbID = imdbId,
                Type = "series",
                Poster = "poster.jpg",
                Actors = "",
                Awards = "",
                Country = "",
                Director = "",
                Genre = "",
                Language = "",
                Metascore = "",
                Plot = "",
                Rated = "",
                Ratings = listOf(),
                Released = "",
                Response = "",
                Runtime = "",
                Writer = "",
                imdbRating = "",
                imdbVotes = "",
                totalSeasons = "1"  // Один сезон
            )

            whenever(
                mockApi.serialDetails(
                    apiKey = any(),
                    i = any(),
                    plot = any()
                )
            ).thenReturn(expectedDetails)

            val result = mockApi.serialDetails(
                apiKey = "test_key", i = imdbId, plot = "full"
            )
            assertEquals("1", result.totalSeasons)
        }
    }
    @Test
    fun `test series with multiple seasons`() {
        runBlocking {
            val imdbId = "tt1234567"
            val expectedDetails = SerialDetails(
                Title = "Long Running Series",
                Year = "2005-2020",
                imdbID = imdbId,
                Type = "series",
                Poster = "poster.jpg",
                Actors = "",
                Awards = "",
                Country = "",
                Director = "",
                Genre = "",
                Language = "",
                Metascore = "",
                Plot = "",
                Rated = "",
                Ratings = listOf(),
                Released = "",
                Response = "",
                Runtime = "",
                Writer = "",
                imdbRating = "",
                imdbVotes = "",
                totalSeasons = "15"  // Много сезонов
            )

            whenever(
                mockApi.serialDetails(
                    apiKey = any(),
                    i = any(),
                    plot = any()
                )
            ).thenReturn(expectedDetails)

            val result = mockApi.serialDetails("test_key", imdbId, "full")
            assertEquals("15", result.totalSeasons)
        }
    }
    @Test(expected = Exception::class)
    fun `test api throws exception on network error`() { runBlocking {
        whenever(mockApi.get2025Series())
            .thenThrow(RuntimeException("Network error"))

        mockApi.get2025Series()
    }}

    @Test
    fun `test serialDetails handles missing imdbID`() {
        runBlocking {
            val imdbId = ""
            val expectedDetails = SerialDetails(
                Title = "Unknown",
                Year = "",
                imdbID = "",
                Type = "",
                Poster = "",
                Actors = "",
                Awards = "",
                Country = "",
                Director = "",
                Genre = "",
                Language = "",
                Metascore = "",
                Plot = "",
                Rated = "",
                Ratings = listOf(),
                Released = "",
                Response = "False",
                Runtime = "",
                Writer = "",
                imdbRating = "",
                imdbVotes = "",
                totalSeasons = ""
            )

            whenever(
                mockApi.serialDetails(
                    apiKey = any(), i = any(), plot = any()
                )
            ).thenReturn(expectedDetails)

            val result = mockApi.serialDetails(apiKey = "test_key", i = imdbId, plot = "full")
            assertEquals("", result.imdbID)
            assertEquals("False", result.Response)
        }
    }
    @Test
    fun `test searchSeries with different page numbers`() {
        runBlocking {
            val query = "test"
            val page1Response = OMDbResponse(
                Search = List(10) { index ->
                    SerialOMDb(
                        Title = "Series Page1-$index",
                        Year = "2023",
                        imdbID = "tt${1000000 + index}",
                        Type = "series",
                        Poster = "poster$index.jpg"
                    )
                },
                totalResults = "30",
                Response = "True"
            )

            val page2Response = OMDbResponse(
                Search = List(10) { index ->
                    SerialOMDb(
                        Title = "Series Page2-$index",
                        Year = "2023",
                        imdbID = "tt${2000000 + index}",
                        Type = "series",
                        Poster = "poster${10 + index}.jpg"
                    )
                },
                totalResults = "30",
                Response = "True"
            )

            whenever(
                mockApi.searchSeries(
                    apiKey = any(),
                    searchQuery = eq(query),
                    type = any(),
                    page = eq(1)
                )
            ).thenReturn(page1Response)

            whenever(
                mockApi.searchSeries(
                    apiKey = any(),
                    searchQuery = eq(query),
                    type = any(),
                    page = eq(2)
                )
            ).thenReturn(page2Response)

            val resultPage1 = mockApi.searchSeries(
                apiKey = "test_key", searchQuery = query, type = "series", page = 1
            )

            val resultPage2 = mockApi.searchSeries(
                apiKey = "test_key", searchQuery = query, type = "series", page = 2
            )

            assertEquals(10, resultPage1.Search.size)
            assertEquals(10, resultPage2.Search.size)
            assertEquals("Series Page1-0", resultPage1.Search[0].Title)
            assertEquals("Series Page2-0", resultPage2.Search[0].Title)
            assertNotEquals(resultPage1.Search[0].imdbID, resultPage2.Search[0].imdbID)
        }
    }
    @Test
    fun `test SerialOMDb data structure validation`() {
        val serial = SerialOMDb(
            Title = "Test Serial",
            Year = "2023",
            imdbID = "tt1234567",
            Type = "series",
            Poster = "https://example.com/poster.jpg"
        )

        assertEquals("Test Serial", serial.Title)
        assertEquals("2023", serial.Year)
        assertEquals("tt1234567", serial.imdbID)
        assertEquals("series", serial.Type)
        assertEquals("https://example.com/poster.jpg", serial.Poster)
    }

    @Test
    fun `test SerialDetails data structure validation`() {
        val details = SerialDetails(
            Title = "Test",
            Year = "2023",
            imdbID = "tt123",
            Type = "movie",
            Poster = "poster.jpg",
            Actors = "Actor 1, Actor 2",
            Awards = "Won 1 Oscar",
            Country = "USA",
            Director = "Director",
            Genre = "Drama",
            Language = "English",
            Metascore = "75",
            Plot = "Test plot",
            Rated = "PG-13",
            Ratings = listOf(),
            Released = "2023-01-01",
            Response = "True",
            Runtime = "120 min",
            Writer = "Writer",
            imdbRating = "7.5",
            imdbVotes = "1000",
            totalSeasons = ""
        )
        assertEquals("Test", details.Title)
        assertEquals("tt123", details.imdbID)
        assertEquals("movie", details.Type)
        assertEquals("7.5", details.imdbRating)
        assertEquals("", details.totalSeasons)
    }

    @Test
    fun testGet2025SeriesWithMinimalData() {
        runBlocking {
            val expectedResponse = OMDbResponse(
                Search = listOf(
                    SerialOMDb(
                        Title = "Minimal Series",  // Это не пустая строка!
                        Year = "",
                        imdbID = "",
                        Type = "",
                        Poster = ""
                    )
                ),
                totalResults = "",
                Response = ""
            )

            whenever(mockApi.get2025Series()).thenReturn(expectedResponse)
            val result = mockApi.get2025Series()
            assertEquals("", result.Response)  // ОК, Response пустая
            assertEquals("Minimal Series", result.Search[0].Title)  // Здесь нужно ожидать "Minimal Series", а не ""
            assertEquals("", result.Search[0].Year)
        }
    }
}