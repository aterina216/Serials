package com.example.serials.data.repository

import android.util.Log
import androidx.room.util.newStringBuilder
import com.example.serials.data.db.dao.SerialDao
import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.mapper.ConverterResponseFromEntity
import com.example.serials.data.remote.api.OMDbApi
import com.example.serials.data.remote.dto.SerialDetails

class SerialsRepository(
    private val dao: SerialDao,
    private val api: OMDbApi,
    private val mapper: ConverterResponseFromEntity
) {


    suspend fun loadSerialsFromApi(page: Int = 1): List<SerialEntity> {
        Log.d("CACHE_DEBUG", "🔄 Начало загрузки NEW, стр. $page")
        val serials = dao.getSerialsFromCategory("NEW")

        Log.d("Repository", "🌐 Начинаем загрузку из API")

        return try {
            val response = api.get2025Series(page = page)
            if (response.Response == "True") {
                Log.d("Repository", "✅ API ответ успешен, сериалов: ${response.Search?.size ?: 0}")
                val entities = response.Search.map { serial ->
                    mapper.convertSerialOMDBFromEntity(serial, "NEW")
                } ?: emptyList()

                Log.d("Repository", "💾 Сохраняем в БД: ${entities.size} сериалов")
                addOnlyNewSerials(entities)
                entities
            } else {
                Log.e("Repository", "❌ API ошибка: ${response}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CACHE_DEBUG", "💥 Сетевая ошибка: ${e.message}")
            Log.d("CACHE_DEBUG", "🆘 Возвращаем кэш при ошибке: ${serials.size} элементов")
            return serials
        }
    }

    private suspend fun addOnlyNewSerials(entities: List<SerialEntity>) {
        val imbds = dao.getAllImdbIds()

        val serialsToAdd = entities.filter { it.imdbID !in imbds }

        if(serialsToAdd.isNotEmpty()) {
            dao.insertSerialsToDB(serialsToAdd)
        }
    }

    suspend fun getSerialDetails(imbd: String): SerialDetails? {

        return try {
            val serialDetails = api.serialDetails(i = imbd)
            if (serialDetails.Response == "True") {
                serialDetails
            } else null
        } catch (e: Exception) {
            println("${e.message}")
            null
        }

    }

    suspend fun searchSeries(query: String, page: Int = 1): List<SerialEntity> {
        return try {
            val result = api.searchSeries(searchQuery = query, page = page)
            if (result.Response == "True") {
                val entities = result.Search?.map { serial ->
                    mapper.convertSerialOMDBFromEntity(serial)
                } ?: emptyList()
                entities  // Возвращаем entities, а не emptyList()!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", "❌ Ошибка поиска: ${e.message}")
            emptyList()
        }
    }

    suspend fun loadSerialsFromCategories(category: String,
                                          page: Int = 1): List<SerialEntity> {
        Log.d("CACHE_DEBUG", "🔄 Начало загрузки категории: $category, стр. $page")
        val cache = dao.getSerialsFromCategory(category)
        Log.d("CACHE_DEBUG", "📊 Кэш в БД для '$category': ${cache.size} элементов")

        return try {

            val result = api.loadserialsFromCategories(category = category, page = page)
            if (result.Response == "True") {
                val entities = result.Search?.map { serial ->
                    mapper.convertSerialOMDBFromEntity(serial, category)
                } ?: emptyList()
                addOnlyNewSerials(entities)
                entities
            }
            else emptyList()
        }
        catch (e: Exception) {
            Log.e("CACHE_DEBUG", "💥 Сетевая ошибка: ${e.message}")
            Log.d("CACHE_DEBUG", "🆘 Возвращаем кэш при ошибке: ${cache.size} элементов")
            cache
        }
    }

    suspend fun updateSerialStatus(id: String, status: String?) {
        Log.d("DEBUG_DB", "Обновление статуса: id=$id, status=$status")

        val existing = dao.getSerialByImdbId(id)
        if(existing != null) {
            Log.d("DEBUG_DB", "Запись найдена: ${existing.Title}")
            dao.updateSerialStatus(id, status)
            Log.d("DEBUG_DB", "Статус обновлен")

            val afterUpdate = dao.getSerialByImdbId(id)
            Log.d("DEBUG_DB", "После обновления статус: ${afterUpdate?.status}")
        }
        else {
            Log.e("DEBUG_DB", "Запись с imdbID=$id не найдена в базе!")
        }
    }

    suspend fun getSerialStatus(id: String): String? {
        Log.d("DEBUG_DB", "Запрос статуса для: $id")
        val status = dao.getStatus(id)
        Log.d("DEBUG_DB", "Получен статус: $status")
        return status
    }

    suspend fun getSerialsFromStatus(status: String?): List<SerialEntity> {
        return dao.getSerialsByStatus(status)
    }

    suspend fun debugDatabase() {
        val totalCount = dao.getTotalCount()
        Log.d("DEBUG_DB", "Всего записей в базе: $totalCount")

        val sampleSerials = dao.getSerialsFromCategory("NEW").take(5)
        sampleSerials.forEach { serial ->
            Log.d("DEBUG_DB", "Сериал: ${serial.Title}, imdbID: ${serial.imdbID}, статус: ${serial.status}")
        }
    }
}
