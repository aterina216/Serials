package com.example.serials.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.remote.api.RetrofitClient
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.data.remote.dto.SerialOMDb
import com.example.serials.data.repository.SerialsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SerialsViewModel(private val repository: SerialsRepository): ViewModel() {

    private var serialsList = MutableStateFlow<List<SerialEntity>>(emptyList())
    val _serialList: StateFlow<List<SerialEntity>> = serialsList.asStateFlow()

    var currentserial = MutableStateFlow<SerialDetails?>(null)

    private var _searchResult = MutableStateFlow<List<SerialEntity>>(emptyList())
    val searchResult: StateFlow<List<SerialEntity>> = _searchResult


    init {
        Log.d("ViewModel", "🚀 ViewModel создан")
        loadSerialsFromDB()
    }

    fun loadSerialDetails(imdb: String) {
        Log.d("ViewModel", "🔵 loadSerialDetails вызван с imdb: $imdb")
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "🔄 Начинаем загрузку деталей...")
                val details = repository.getSerialDetails(imdb)
                Log.d("ViewModel", "✅ Детали получены: ${details?.Title ?: "NULL"}")
                currentserial.value = details
            } catch (e: Exception) {
                Log.e("ViewModel", "❌ Ошибка загрузки деталей", e)
                println("Ошибка: ${e.message}")
            }
        }
    }
    fun loadSerialsFromDB() {
        Log.d("ViewModel", "🔄 loadSerialsFromDB() вызван")
        viewModelScope.launch {
            try {
                val data = repository.getSerialsFromRepo()
                Log.d("ViewModel", "📊 Получено данных: ${data.size}")
                serialsList.value = data
                Log.d("ViewModel", "✅ StateFlow обновлен")
            }
            catch (e: Exception) {
                Log.e("ViewModel", "💥 Ошибка в ViewModel: ${e.message}")
            }
        }
    }

    fun searchSerials (query: String) {
        viewModelScope.launch {
            val response = repository.searchSeries(query)
            _searchResult.value = response
        }
    }
}