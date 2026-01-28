package com.example.serials

import android.util.Log
import androidx.annotation.OptIn
import com.example.serials.data.db.dao.SerialDao
import com.example.serials.data.db.entity.ReminderEntity
import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.mapper.ConverterResponseFromEntity
import com.example.serials.data.mapper.convertSerialEntityFromDetails
import com.example.serials.data.remote.api.OMDbApi
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.data.remote.dto.SerialOMDb
import com.example.serials.data.remote.response.OMDbResponse
import com.example.serials.data.repository.SerialsRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SerialsRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockDao: SerialDao

    @MockK
    lateinit var mockApi: OMDbApi

    @MockK
    lateinit var mockMapper: ConverterResponseFromEntity

    private lateinit var repository: SerialsRepository

    @Before
    fun setup() {
        // Мокаем статические методы Log.d чтобы избежать "Method d in android.util.Log not mocked"
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0

        repository = SerialsRepository(mockDao, mockApi, mockMapper)
    }

    @Test
    fun `loadSerialsFromApi should return entities on success`() = runTest {
        // 1. Подготовка данных
        val apiResponse = OMDbResponse(
            Search = listOf(
                SerialOMDb(
                    Title = "Series 1",
                    Year = "2025",
                    imdbID = "tt1",
                    Type = "series",
                    Poster = "poster1.jpg"
                ),
                SerialOMDb(
                    Title = "Series 2",
                    Year = "2025",
                    imdbID = "tt2",
                    Type = "series",
                    Poster = "poster2.jpg"
                )
            ),
            totalResults = "2",
            Response = "True"
        )

        val entity1 = SerialEntity(
            imdbID = "tt1",
            Title = "Series 1",
            Year = "2025",
            Poster = "poster1.jpg",
            Type = "series",
            category = "NEW"
        )

        val entity2 = SerialEntity(
            imdbID = "tt2",
            Title = "Series 2",
            Year = "2025",
            Poster = "poster2.jpg",
            Type = "series",
            category = "NEW"
        )

        val existingIds = listOf("tt3", "tt4")

        // 2. Настройка моков MockK - для suspend функций используем coEvery
        coEvery { mockApi.get2025Series(page = 1) } returns apiResponse
        every { mockMapper.convertSerialOMDBFromEntity(any(), "NEW") } returnsMany listOf(entity1, entity2)
        coEvery { mockDao.getAllImdbIds() } returns existingIds
        coEvery { mockDao.getSerialsFromCategory("NEW") } returns emptyList()
        coEvery { mockDao.insertSerialsToDB(any()) } just Runs

        // 3. Выполнение тестируемого метода
        val result = repository.loadSerialsFromApi()

        // 4. Проверки
        assertEquals(2, result.size)
        assertEquals("Series 1", result[0].Title)
        assertEquals("NEW", result[0].category)

        // 5. Верификация вызовов - для suspend функций используем coVerify
        coVerify {
            mockApi.get2025Series(page = 1)
            mockDao.insertSerialsToDB(listOf(entity1, entity2))
        }
        coVerify(exactly = 0) {
            mockDao.insertSerialsToDB(emptyList())
        }
    }

    @Test
    fun `loadSerialsFromApi should return empty list on Api error`() = runTest {
        val apiResponse = OMDbResponse(
            Search = emptyList(),
            totalResults = "0",
            Response = "False"
        )

        coEvery { mockApi.get2025Series(page = 1) } returns apiResponse
        coEvery { mockDao.getSerialsFromCategory("NEW") } returns emptyList()

        val result = repository.loadSerialsFromApi()

        assertTrue(result.isEmpty())
        coVerify { mockApi.get2025Series(page = 1) }
        coVerify(exactly = 0) { mockDao.insertSerialsToDB(any()) }
    }

    @Test
    fun `loadSerialsFromApi should return cache on network exception`() = runTest {
        val cachedSerials = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Cached Series",
                Year = "2024",
                Poster = "poster.jpg",
                Type = "series",
                category = "NEW"
            )
        )

        coEvery { mockApi.get2025Series(page = 1) } throws RuntimeException("Network error")
        coEvery { mockDao.getSerialsFromCategory("NEW") } returns cachedSerials

        val result = repository.loadSerialsFromApi()

        assertEquals(1, result.size)
        assertEquals("Cached Series", result[0].Title)
        coVerify { mockApi.get2025Series(page = 1) }
        coVerify(exactly = 0) { mockDao.insertSerialsToDB(any()) }
    }

    @Test
    fun `getSerialDetails should return details on success`() = runTest {
        val imdbId = "tt1234567"
        val apiDetails = SerialDetails(
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

        val convertedEntity = SerialEntity(
            imdbID = imdbId,
            Title = "Breaking Bad",
            Year = "2008",
            Poster = "poster.jpg",
            Type = "series"
        )

        // Настраиваем все необходимые моки
        coEvery { mockApi.serialDetails(i = imdbId) } returns apiDetails
        coEvery { mockDao.getSerialByImdbId(imdbId) } returns null
        coEvery { mockDao.insertSerialToDB(any()) } just Runs
        coEvery { mockDao.insertSerialsToDB(any()) } just Runs

        val result = repository.getSerialDetails(imdbId)

        assertNotNull(result)
        assertEquals("Breaking Bad", result?.Title)
        assertEquals(imdbId, result?.imdbID)

        coVerify {
            mockApi.serialDetails(i = imdbId)
            // Проверяем, какой именно метод вызывается
            mockDao.insertSerialToDB(any())
        }
        // Если метод не вызывается, можно проверить это
        coVerify(exactly = 0) { mockDao.insertSerialsToDB(any()) }
    }

    @Test
    fun `getSerialDetails should not save if already in DB`() = runTest {
        val imdbId = "tt1234567"
        val existingEntity = SerialEntity(
            imdbID = imdbId,
            Title = "Existing",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series"
        )

        val apiDetails = SerialDetails(
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

        coEvery { mockApi.serialDetails(i = imdbId) } returns apiDetails
        coEvery { mockDao.getSerialByImdbId(imdbId) } returns existingEntity

        val result = repository.getSerialDetails(imdbId)
        assertNotNull(result)
        coVerify { mockApi.serialDetails(i = imdbId) }
        coVerify(exactly = 0) { mockDao.insertSerialsToDB(any()) }
    }

    @Test
    fun `getSerialDetails should return null on Api error`() = runTest {
        val imdbId = "tt1234567"
        val apiDetails = SerialDetails(
            Title = "Error",
            Year = "",
            imdbID = imdbId,
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

        coEvery { mockApi.serialDetails(i = imdbId) } returns apiDetails
        val result = repository.getSerialDetails(imdbId)
        assertNull(result)
    }

    @Test
    fun `searchSeries should return entities on success`() = runTest {
        val query = "breaking bad"
        val apiResponse = OMDbResponse(
            Search = listOf(
                SerialOMDb(
                    Title = "Breaking Bad",
                    Year = "2008",
                    imdbID = "tt0903747",
                    Type = "series",
                    Poster = "poster.jpg"
                )
            ),
            totalResults = "1",
            Response = "True"
        )

        val entity = SerialEntity(
            imdbID = "tt0903747",
            Title = "Breaking Bad",
            Year = "2008",
            Poster = "poster.jpg",
            Type = "series"
        )

        coEvery { mockApi.searchSeries(searchQuery = query, page = 1) } returns apiResponse
        every { mockMapper.convertSerialOMDBFromEntity(any()) } returns entity

        val result = repository.searchSeries(query)

        assertEquals(1, result.size)
        assertEquals("Breaking Bad", result[0].Title)
        coVerify { mockApi.searchSeries(searchQuery = query, page = 1) }
    }

    @Test
    fun `searchSeries should return empty list on no results`() = runTest {
        val query = "asdfghjkl"
        val apiResponse = OMDbResponse(
            Search = emptyList(),
            totalResults = "0",
            Response = "False"
        )

        coEvery { mockApi.searchSeries(searchQuery = query, page = 1) } returns apiResponse
        val result = repository.searchSeries(query)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `searchSeries should return empty list on exception`() = runTest {
        val query = "test"

        coEvery { mockApi.searchSeries(searchQuery = query, page = 1) } throws RuntimeException("Network error")

        val result = repository.searchSeries(query)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadSerialsFromCategories should return entities on success`() = runTest {
        val category = "action"
        val apiResponse = OMDbResponse(
            Search = listOf(
                SerialOMDb(
                    Title = "Action Series",
                    Year = "2023",
                    imdbID = "tt1",
                    Type = "series",
                    Poster = "poster.jpg"
                )
            ),
            totalResults = "1",
            Response = "True"
        )

        val entity = SerialEntity(
            imdbID = "tt1",
            Title = "Action Series",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series",
            category = category
        )

        coEvery { mockApi.loadserialsFromCategories(category = category, page = 1) } returns apiResponse
        every { mockMapper.convertSerialOMDBFromEntity(any(), category) } returns entity
        coEvery { mockDao.getAllImdbIds() } returns emptyList()
        coEvery { mockDao.getSerialsFromCategory(category) } returns emptyList()
        coEvery { mockDao.insertSerialsToDB(any()) } just Runs

        val result = repository.loadSerialsFromCategories(category)

        assertEquals(1, result.size)
        assertEquals("Action Series", result[0].Title)
        assertEquals(category, result[0].category)

        coVerify { mockApi.loadserialsFromCategories(category = category, page = 1) }
        coVerify { mockDao.insertSerialsToDB(any()) }
    }

    @Test
    fun `updateSerialStatus should update when serial exists`() = runTest {
        val imdbId = "tt1234567"
        val status = "Watching"
        val existingEntity = SerialEntity(
            imdbID = imdbId,
            Title = "Test Series",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series"
        )

        coEvery { mockDao.getSerialByImdbId(imdbId) } returns existingEntity
        coEvery { mockDao.updateSerialStatus(imdbId, status) } just Runs

        repository.updateSerialStatus(imdbId, status)
        coVerify { mockDao.updateSerialStatus(imdbId, status) }
    }

    @Test
    fun `updateSerialStatus should log error when serial not found`() = runTest {
        val imdbId = "tt9999999"
        val status = "Watching"

        coEvery { mockDao.getSerialByImdbId(imdbId) } returns null

        repository.updateSerialStatus(imdbId, status)

        coVerify(exactly = 0) { mockDao.updateSerialStatus(any(), any()) }
    }

    @Test
    fun `updateSerialStatus should handle null status`() = runTest {
        val imdbId = "tt1234567"
        val existingEntity = SerialEntity(
            imdbID = imdbId,
            Title = "Test Series",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series"
        )

        coEvery { mockDao.getSerialByImdbId(imdbId) } returns existingEntity
        coEvery { mockDao.updateSerialStatus(imdbId, null) } just Runs

        repository.updateSerialStatus(imdbId, null)
        coVerify { mockDao.updateSerialStatus(imdbId, null) }
    }

    @Test
    fun `getSerialStatus should return status from DAO`() = runTest {
        val imdbId = "tt1234567"
        val expectedStatus = "Watching"

        coEvery { mockDao.getStatus(imdbId) } returns expectedStatus
        val result = repository.getSerialStatus(imdbId)
        assertEquals(expectedStatus, result)
        coVerify { mockDao.getStatus(imdbId) }
    }

    @Test
    fun `getSerialsFromStatus should return list from DAO`() = runTest {
        val status = "Watching"
        val expectedList = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Series 1",
                Year = "2023",
                Poster = "poster1.jpg",
                Type = "series",
                status = status
            )
        )

        coEvery { mockDao.getSerialsByStatus(status) } returns expectedList
        val result = repository.getSerialsFromStatus(status)

        assertEquals(1 , result.size)
        assertEquals(status, result[0].status)
        coVerify { mockDao.getSerialsByStatus(status) }
    }

    @Test
    fun `getSerialsFromStatus should handle null status`() = runTest {
        val expectedList = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Series 1",
                Year = "2023",
                Poster = "poster1.jpg",
                Type = "series",
                status = null
            )
        )

        coEvery { mockDao.getSerialsByStatus(null) } returns expectedList
        val result = repository.getSerialsFromStatus(null)
        assertEquals(1, result.size)
        assertNull(result[0].status)
    }

    @Test
    fun `updateCurrentTime should call DAO with correct parameters`() = runTest {
        val imbdId = "tt234567"
        val watchdAt = System.currentTimeMillis()

        coEvery { mockDao.updateCurrentTime(imbdId, watchdAt) } just Runs

        repository.updateCurrentTime(watchdAt, imbdId)
        coVerify { mockDao.updateCurrentTime(imbdId, watchdAt) }
    }

    @Test
    fun `getHistorySerials should return list from DAO`() = runTest {
        val expectedList = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Watched Series",
                Year = "2023",
                Poster = "poster.jpg",
                Type = "series",
                watchedAt = System.currentTimeMillis()
            )
        )

        coEvery { mockDao.getHistorySerials() } returns expectedList

        val result = repository.getHistorySerials()
        assertEquals(1 ,result.size)
        assertNotNull(result[0].watchedAt)
        coVerify { mockDao.getHistorySerials() }
    }

    @Test
    fun `clearHistory should call DAO`() = runTest {
        coEvery { mockDao.clearHistory() } just Runs

        repository.clearHistory()
        coVerify { mockDao.clearHistory() }
    }

    @Test
    fun `addReminderBook should call DAO with reminder`() = runTest {
        val reminder = ReminderEntity(
            imdbID = "tt1234567",
            Title = "Test Reminder",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series",
            time = System.currentTimeMillis() + 1000000,
            isActive = true
        )

        coEvery { mockDao.addReminderBook(reminder) } just Runs

        repository.addReminderBook(reminder)
        coVerify { mockDao.addReminderBook(reminder) }
    }

    @Test
    fun `deleteInactiveReminders should call DAO`() = runTest {
        coEvery { mockDao.deleteInactiveReminders() } just Runs

        repository.deleteInactiveReminders()
        coVerify { mockDao.deleteInactiveReminders() }
    }

    @Test
    fun `getReminders should return active reminders from DAO`() = runTest {
        val expectedReminders = listOf(
            ReminderEntity(
                imdbID = "tt1",
                Title = "Reminder 1",
                Year = "2023",
                Poster = "poster.jpg",
                Type = "series",
                time = System.currentTimeMillis() + 1000000,
                isActive = true
            )
        )

        coEvery { mockDao.getActiveReminders() } returns expectedReminders

        val result = repository.getReminders()

        assertEquals(1, result.size)
        assertTrue(result[0].isActive)
        coVerify { mockDao.getActiveReminders() }
    }

    @Test
    fun `deactivateExpiredReminders should call DAO with current time`() = runTest {
        val currentTime = System.currentTimeMillis()
        coEvery { mockDao.deactivateExpiredReminders(currentTime) } just Runs

        repository.deactivateExpiredReminders(currentTime)
        coVerify { mockDao.deactivateExpiredReminders(currentTime) }
    }

    @Test
    fun `deleteNotification should call DAO with id`() = runTest {
        val imdbId = "tt1234567"
        coEvery { mockDao.deleteReminder(imdbId) } just Runs

        repository.deleteNotification(imdbId)
        coVerify { mockDao.deleteReminder(imdbId) }
    }

    @Test
    fun `updateReminder should call DAO with id and time`() = runTest {
        val imbdId = "tt1234567"
        val newTime = System.currentTimeMillis() + 2000000

        coEvery { mockDao.updateReminderTime(imbdId, newTime) } just Runs

        repository.updateReminderTime(imbdId, newTime)
        coVerify { mockDao.updateReminderTime(imbdId, newTime) }
    }

    @Test
    fun `debugDatabase should call DAO methods`() = runTest {
        coEvery { mockDao.getTotalCount() } returns 5
        coEvery { mockDao.getSerialsFromCategory("NEW") } returns emptyList()

        repository.debugDatabase()

        coVerify {
            mockDao.getTotalCount()
            mockDao.getSerialsFromCategory("NEW")
        }
    }

    @Test
    fun `addOnlyNewSerials should only add new serials`() = runTest {
        val existingIds = listOf("tt1", "tt2")
        val newEntity = SerialEntity(
            imdbID = "tt3",
            Title = "New Series",
            Year = "2025",
            Poster = "poster.jpg",
            Type = "series",
            category = "NEW"
        )

        val existingEntity = SerialEntity(
            imdbID = "tt1",
            Title = "Existing Series",
            Year = "2024",
            Poster = "poster.jpg",
            Type = "series",
            category = "NEW"
        )

        val apiResponse = OMDbResponse(
            Search = listOf(
                SerialOMDb("New Series", "2025", "tt3", "series", "poster.jpg"),
                SerialOMDb("Existing Series", "2024", "tt1", "series", "poster.jpg")
            ),
            totalResults = "2",
            Response = "True"
        )

        coEvery { mockApi.get2025Series(page = 1) } returns apiResponse
        every { mockMapper.convertSerialOMDBFromEntity(any(), "NEW") } returnsMany listOf(newEntity, existingEntity)
        coEvery { mockDao.getAllImdbIds() } returns existingIds
        coEvery { mockDao.getSerialsFromCategory("NEW") } returns emptyList()
        coEvery { mockDao.insertSerialsToDB(any()) } just Runs

        repository.loadSerialsFromApi()
        coVerify { mockDao.insertSerialsToDB(listOf(newEntity)) }
    }
}