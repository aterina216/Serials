package com.example.serials

import com.example.serials.data.mapper.ConverterResponseFromEntity
import com.example.serials.data.mapper.convertSerialDetailsToReminderEntity
import com.example.serials.data.mapper.convertSerialEntityFromDetails
import com.example.serials.data.remote.dto.Rating
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.data.remote.dto.SerialOMDb
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConverterTest {

    private val converter = ConverterResponseFromEntity()

    @Test
    fun `convertSerialOMDBFromEntity should correctle map all fields`() {
        val serialOMDb = SerialOMDb(
            Poster = "poster_url",
            Title = "Test Serial",
            Type = "series",
            Year = "2020",
            imdbID = "tt1234567"
        )

        val expectedCategory = "Action"

        val result = converter.convertSerialOMDBFromEntity(serialOMDb, expectedCategory)

        assertEquals(serialOMDb.Poster, result.Poster)
        assertEquals(serialOMDb.Title, result.Title)
        assertEquals(serialOMDb.Type, result.Type)
        assertEquals(serialOMDb.Year, result.Year)
        assertEquals(serialOMDb.imdbID, result.imdbID)
        assertEquals(expectedCategory, result.category)
    }

    @Test
    fun `test convertSerialOMDBFromEntity without category`() {
        val serialOMDb = SerialOMDb(
            Poster = "poster2.jpg",
            Title = "Game of Thrones",
            Type = "series",
            Year = "2011",
            imdbID = "tt0944947"
        )

        val result = converter.convertSerialOMDBFromEntity(serialOMDb)

        assertEquals("Game of Thrones", result.Title)
        assertEquals("2011", result.Year)
        assertNull(result.category)
    }

    @Test
    fun `test convertSerialEntityFromDetails with status`() {
        val serialDetails = SerialDetails(
            Actors = "Bryan Cranston",
            Awards = "Won 2 Primetime Emmys",
            Country = "USA",
            Director = "Vince Gilligan",
            Genre = "Crime, Drama, Thriller",
            Language = "English, Spanish",
            Metascore = "89",
            Plot = "A chemistry teacher diagnosed with cancer...",
            Poster = "poster3.jpg",
            Rated = "TV-MA",
            Ratings = listOf(Rating("Internet Movie Database", "9.5/10")),
            Released = "20 Jan 2008",
            Response = "True",
            Runtime = "49 min",
            Title = "Breaking Bad",
            Type = "series",
            Writer = "Vince Gilligan",
            Year = "2008",
            imdbID = "tt0903747",
            imdbRating = "9.5",
            imdbVotes = "1,800,000",
            totalSeasons = "5"
        )

        val status = "Watching"

        val result = convertSerialEntityFromDetails(serialDetails, status)

        assertEquals("poster3.jpg", result.Poster)
        assertEquals("Breaking Bad", result.Title)
        assertEquals("series", result.Type)
        assertEquals("2008", result.Year)
        assertEquals("Watching", result.status)
        assertEquals("tt0903747", result.imdbID)
        assertNull(result.category)
    }

    @Test
    fun `test converSerialEntityFromDetails without status`() {
        val serialDetails = SerialDetails(
            Actors = "Pedro Pascal",
            Awards = "Won 8 Primetime Emmys",
            Country = "USA",
            Director = "Craig Mazin",
            Genre = "Drama, Horror, Sci-Fi",
            Language = "English",
            Metascore = "82",
            Plot = "After a global pandemic destroys civilization...",
            Poster = "poster4.jpg",
            Rated = "TV-MA",
            Ratings = listOf(Rating("Internet Movie Database", "8.8/10")),
            Released = "15 Jan 2023",
            Response = "True",
            Runtime = "55 min",
            Title = "The Last of Us",
            Type = "series",
            Writer = "Craig Mazin, Neil Druckmann",
            Year = "2023",
            imdbID = "tt3581920",
            imdbRating = "8.8",
            imdbVotes = "400,000",
            totalSeasons = "1"
        )

        val result = convertSerialEntityFromDetails(serialDetails)

        assertEquals("The Last of Us", result.Title)
        assertEquals("2023", result.Year)
        assertNull(result.status)
    }

    @Test
    fun `test convertSerialDetailsToReminderEntity`() {
        val serialDetails = SerialDetails(
            Actors = "Henry Cavill",
            Awards = "Nominated for 1 BAFTA Award",
            Country = "USA, Poland",
            Director = "Lauren Schmidt Hissrich",
            Genre = "Action, Adventure, Drama",
            Language = "English",
            Metascore = "67",
            Plot = "Geralt of Rivia, a mutated monster-hunter...",
            Poster = "poster5.jpg",
            Rated = "TV-MA",
            Ratings = listOf(Rating("Internet Movie Database", "8.2/10")),
            Released = "20 Dec 2019",
            Response = "True",
            Runtime = "60 min",
            Title = "The Witcher",
            Type = "series",
            Writer = "Lauren Schmidt Hissrich",
            Year = "2019",
            imdbID = "tt5180504",
            imdbRating = "8.2",
            imdbVotes = "500,000",
            totalSeasons = "3"
        )
        val time = 1672531200000L

        val result = convertSerialDetailsToReminderEntity(serialDetails, time)

        assertEquals("poster5.jpg", result.Poster)
        assertEquals("The Witcher", result.Title)
        assertEquals("series", result.Type)
        assertEquals("2019", result.Year)
        assertEquals("tt5180504", result.imdbID)
        assertTrue(result.isActive)
        assertEquals(1672531200000L, result.time)
    }

    @Test
    fun `test with minimal data`() {
        val serialOMDb = SerialOMDb(
            Poster = "",
            Title = "Test Movie",
            Type = "",
            Year = "",
            imdbID = "tt1234567"
        )

        val result = converter.convertSerialOMDBFromEntity(serialOMDb, null)
        assertEquals("Test Movie", result.Title)
        assertEquals("", result.Year)
        assertEquals("", result.Poster)
        assertEquals("", result.Type)
        assertEquals("tt1234567", result.imdbID)
        assertNull(result.category)
    }

    @Test
    fun `test with NA poster`() {
        val serialDetails = SerialDetails(
            Actors = "",
            Awards = "",
            Country = "",
            Director = "",
            Genre = "",
            Language = "",
            Metascore = "",
            Plot = "",
            Poster = "N/A", // Важный случай!
            Rated = "",
            Ratings = emptyList(),
            Released = "",
            Response = "",
            Runtime = "",
            Title = "Unknown Series",
            Type = "",
            Writer = "",
            Year = "",
            imdbID = "tt0000000",
            imdbRating = "",
            imdbVotes = "",
            totalSeasons = ""
        )

        val result = convertSerialEntityFromDetails(serialDetails)
        assertEquals("N/A", result.Poster)
        assertEquals("Unknown Series", result.Title)
    }
}