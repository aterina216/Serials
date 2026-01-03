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
                dao.insertSerialsToDB(entities)
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
                dao.insertSerialsToDB(entities)
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
}
