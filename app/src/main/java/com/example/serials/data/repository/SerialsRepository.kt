package com.example.serials.data.repository

import android.util.Log
import com.example.serials.data.db.dao.SerialDao
import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.mapper.ConverterResponseFromEntity
import com.example.serials.data.remote.api.OMDbApi
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.data.remote.dto.SerialOMDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class SerialsRepository(
    private val dao: SerialDao,
    private val api: OMDbApi,
    private val mapper: ConverterResponseFromEntity
) {

    suspend fun loadSerialsFromDb(): List<SerialEntity> {

        return try {
            Log.d("Repository", "🔍 Начинаем загрузку из БД")
            val serialsFromDb = dao.getSerilals()
            Log.d("Repository", "📀 Получено из БД: ${serialsFromDb} сериалов")
            return serialsFromDb.first()
        } catch (e: Exception) {
            Log.e("Repository", "❌ Ошибка БД: ${e.message}")
            emptyList()
        }
    }

    suspend fun loadSerialsFromApi(page: Int = 1): List<SerialEntity> {
        Log.d("Repository", "🌐 Начинаем загрузку из API")
        val response = api.get2025Series(page = page)
        return try {
            if (response.Response == "True") {
                Log.d("Repository", "✅ API ответ успешен, сериалов: ${response.Search?.size ?: 0}")
                val entities = response.Search.map { serial ->
                    mapper.convertSerialOMDBFromEntity(serial)
                } ?: emptyList()

                Log.d("Repository", "💾 Сохраняем в БД: ${entities.size} сериалов")
                dao.insertSerialsToDB(entities)
                entities
            } else {
                Log.e("Repository", "❌ API ошибка: ${response}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", "💥 Ошибка сети: ${e.message}")
            println("${e.message}")
            emptyList()
        }
    }

    suspend fun getSerialsFromRepo(): List<SerialEntity> {
        Log.d("Repository", "🔄 getSerialsFromRepo() вызван")
        val serialFromDB = loadSerialsFromDb()
        if (serialFromDB.isNotEmpty()) {
            Log.d("Repository", "🎯 Возвращаем данные из БД")
            return serialFromDB
        } else {
            Log.d("Repository", "🔄 БД пуста, загружаем из API")
            return loadSerialsFromApi()
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
        return try {
            val result = api.loadserialsFromCategories(category = category, page = page)
            if (result.Response == "True") {
                val entities = result.Search?.map { serial ->
                    mapper.convertSerialOMDBFromEntity(serial)
                } ?: emptyList()
                entities
            }
            else emptyList()
        }
        catch (e: Exception) {
            emptyList()
        }
    }
}
