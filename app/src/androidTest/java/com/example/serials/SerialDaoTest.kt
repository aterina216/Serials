package com.example.serials

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.serials.data.db.dao.SerialDao
import com.example.serials.data.db.database.SerialsDatabase
import com.example.serials.data.db.entity.ReminderEntity
import com.example.serials.data.db.entity.SerialEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SerialDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: SerialsDatabase
    private lateinit var serialDao: SerialDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SerialsDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        serialDao = database.dao()
    }

    @After
    fun teardown() = database.close()

    @Test
    fun `insertSerialToDB_and_retrieve`() = runBlocking {
        val serial = SerialEntity(
            imdbID = "tt1234567",
            Title = "Test Serial",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "movie",
            category = "Action",
            status = null,
            watchedAt = null
        )

        serialDao.insertSerialToDB(serial)
        val retrieved = serialDao.getSerialByImdbId("tt1234567")

        assert(retrieved != null)
        assert(retrieved?.Title == "Test Serial")
        assert(retrieved?.category == "Action")
    }

    @Test
    fun insertMultipleSerials_and_getAll() = runBlocking {
        val serials = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Serial 1",
                Year = "2021",
                Poster = "poster1.jpg",
                Type = "series",
                category = "Drama"
            ),
            SerialEntity(
                imdbID = "tt2",
                Title = "Serial 2",
                Year = "2022",
                Poster = "poster2.jpg",
                Type = "movie",
                category = "Action"
            )
        )

        serialDao.insertSerialsToDB(serials)
        val allSerials = serialDao.getSerilals().first()

        assert(allSerials.size == 2)
        assert(allSerials.any() {it.imdbID == "tt1"})
        assert(allSerials.any() {it.imdbID == "tt2"})
    }

    @Test
    fun getSerialsFromCategory() = runBlocking {
        val actionSerial = SerialEntity(
            imdbID = "tt1",
            Title = "Action Serial",
            Year = "2023",
            Poster = "poster1.jpg",
            Type = "movie",
            category = "Action"
        )

        val dramaSerial = SerialEntity(
            imdbID = "tt2",
            Title = "Drama Serial",
            Year = "2023",
            Poster = "poster2.jpg",
            Type = "series",
            category = "Drama"
        )

        serialDao.insertSerialsToDB(listOf(actionSerial, dramaSerial))
        val actionSerials = serialDao.getSerialsFromCategory("Action")

        assert(actionSerials.size == 1)
        assert(actionSerials[0].Title == "Action Serial")
    }

    @Test
    fun updateSerialStatus() = runBlocking {
        val serial = SerialEntity(
            imdbID = "tt123",
            Title = "Test",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "movie",
            category = null,
            status = null
        )

        serialDao.insertSerialToDB(serial)

        serialDao.updateSerialStatus("tt123", "Watching")
        val updated = serialDao.getSerialByImdbId("tt123")
        val status = serialDao.getStatus("tt123")

        assert(updated?.status == "Watching")
        assert(status == "Watching")
    }

    @Test
    fun getSerialsByStatus() = runBlocking {
        val watchingSerial = SerialEntity(
            imdbID = "tt1",
            Title = "Watching Serial",
            Year = "2023",
            Poster = "poster1.jpg",
            Type = "series",
            status = "Watching"
        )

        val completedSerial = SerialEntity(
            imdbID = "tt2",
            Title = "Completed Serial",
            Year = "2023",
            Poster = "poster2.jpg",
            Type = "movie",
            status = "Completed"
        )

        serialDao.insertSerialsToDB(listOf(watchingSerial, completedSerial))
        val watchingSerials = serialDao.getSerialsByStatus("Watching")

        assert(watchingSerials.size == 1)
        assert(watchingSerials[0].imdbID == "tt1")
    }

    @Test
    fun getTotalCount() = runBlocking {
        val serials = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Serial 1",
                Year = "2021",
                Poster = "poster1.jpg",
                Type = "series"
            ),
            SerialEntity(
                imdbID = "tt2",
                Title = "Serial 2",
                Year = "2022",
                Poster = "poster2.jpg",
                Type = "movie"
            )
        )

        serialDao.insertSerialsToDB(serials)
        val count = serialDao.getTotalCount()

        assert(count == 2)
    }

    @Test
    fun getAllImdbIds() = runBlocking {
        val serials = listOf(
            SerialEntity(
                imdbID = "tt1",
                Title = "Serial 1",
                Year = "2021",
                Poster = "poster1.jpg",
                Type = "series"
            ),
            SerialEntity(
                imdbID = "tt2",
                Title = "Serial 2",
                Year = "2022",
                Poster = "poster2.jpg",
                Type = "movie"
            )
        )

        serialDao.insertSerialsToDB(serials)
        val ids = serialDao.getAllImdbIds()

        assert(ids.size == 2)
        assert(ids.contains("tt1"))
        assert(ids.contains("tt2"))
    }

    @Test
    fun updateCurrentTime_and_getHistory() = runBlocking {
        val serial = SerialEntity(
            imdbID = "tt123",
            Title = "Test Serial",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "movie",
            watchedAt = null
        )

        val currentTime = System.currentTimeMillis()

        serialDao.insertSerialToDB(serial)
        serialDao.updateCurrentTime("tt123", currentTime)

        val history = serialDao.getHistorySerials()

        assert(history.size == 1)
        assert(history[0].watchedAt == currentTime)
    }

    @Test
    fun clearHistory() = runBlocking {
        val serial = SerialEntity(
            imdbID = "tt123",
            Title = "Test Serial",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "movie",
            watchedAt = System.currentTimeMillis()
        )

        serialDao.insertSerialToDB(serial)
        serialDao.clearHistory()

        val history = serialDao.getHistorySerials()
        assert(history.isEmpty())
    }

    @Test
    fun addReminderBook_and_getActiveReminders() = runBlocking {
        val reminder = ReminderEntity(
            imdbID = "tt123",
            Title = "Test Reminder",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series",
            time = System.currentTimeMillis() + 1000000,
            isActive = true
        )
        serialDao.addReminderBook(reminder)
        val activeReminders = serialDao.getActiveReminders()

        assert(activeReminders.size == 1)
        assert(activeReminders[0].imdbID == "tt123")
    }

    @Test
    fun deactivateReminder() = runBlocking {
        val expiredReminder = ReminderEntity(
            imdbID = "tt1",
            Title = "Expired",
            Year = "2023",
            Poster = "poster1.jpg",
            Type = "movie",
            time = System.currentTimeMillis() - 1000000, // Прошедшее время
            isActive = true
        )

        val futureReminder = ReminderEntity(
            imdbID = "tt2",
            Title = "Future",
            Year = "2023",
            Poster = "poster2.jpg",
            Type = "series",
            time = System.currentTimeMillis() + 1000000, // Будущее время
            isActive = true
        )

        serialDao.addReminderBook(expiredReminder)
        serialDao.addReminderBook(futureReminder)

        serialDao.deactivateExpiredReminders(System.currentTimeMillis())
        val activeReminders = serialDao.getActiveReminders()
        assert(activeReminders.size == 1)
        assert(activeReminders[0].imdbID == "tt2")
    }

    @Test
    fun deleteInactiveReminders() = runBlocking {
        val activeReminder = ReminderEntity(
            imdbID = "tt1",
            Title = "Active",
            Year = "2023",
            Poster = "poster1.jpg",
            Type = "movie",
            time = System.currentTimeMillis() + 1000000,
            isActive = true
        )

        val inactiveReminder = ReminderEntity(
            imdbID = "tt2",
            Title = "Inactive",
            Year = "2023",
            Poster = "poster2.jpg",
            Type = "series",
            time = System.currentTimeMillis() - 1000000,
            isActive = false
        )

        serialDao.addReminderBook(activeReminder)
        serialDao.addReminderBook(inactiveReminder)

        serialDao.deleteInactiveReminders()
        val reminders= serialDao.getAllRemindersId()

        assert(reminders.size == 1)
        assert(reminders[0] == "tt1")
    }

    @Test
    fun deleteReminder() = runBlocking {
        val reminder = ReminderEntity(
            imdbID = "tt123",
            Title = "Test",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "movie",
            time = System.currentTimeMillis(),
            isActive = true
        )
        serialDao.addReminderBook(reminder)
        serialDao.deleteReminder("tt123")

        val reminders= serialDao.getAllRemindersId()
        assert(reminders.isEmpty())
    }

    @Test
    fun updateReminderTime() = runBlocking {
        val newTime = System.currentTimeMillis() + 2000000
        val reminder = ReminderEntity(
            imdbID = "tt123",
            Title = "Test",
            Year = "2023",
            Poster = "poster.jpg",
            Type = "series",
            time = System.currentTimeMillis(),
            isActive = true
        )

        serialDao.addReminderBook(reminder)
        serialDao.updateReminderTime("tt123", newTime)

        val updateReminder = serialDao.getActiveReminders().firstOrNull()

        assert(updateReminder != null)
        assert(updateReminder?.time == newTime)
    }

    @Test
    fun deleteAllReminders() = runBlocking {
        val reminders = listOf(
            ReminderEntity(
                imdbID = "tt1",
                Title = "Reminder 1",
                Year = "2023",
                Poster = "poster1.jpg",
                Type = "movie",
                time = System.currentTimeMillis(),
                isActive = true
            ),
            ReminderEntity(
                imdbID = "tt2",
                Title = "Reminder 2",
                Year = "2023",
                Poster = "poster2.jpg",
                Type = "series",
                time = System.currentTimeMillis(),
                isActive = true
            )
        )

        reminders.forEach {
            serialDao.addReminderBook(it)
        }
        serialDao.deleteAllReminders()
        val allReminders= serialDao.getActiveReminders()
        assert(allReminders.isEmpty())
    }
}