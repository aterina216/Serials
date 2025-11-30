package com.example.serials.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serials.data.remote.api.RetrofitClient
import com.example.serials.data.remote.dto.SerialOMDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SerialsViewModel: ViewModel() {

    private var serialsList = MutableStateFlow<List<SerialOMDb>>(emptyList())
    val _serialList: StateFlow<List<SerialOMDb>> = serialsList.asStateFlow()

    init {
        loadSerialsFromDB()
    }

    fun loadSerialsFromDB() {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "🎯 Начали загрузку сериалов")
                val response = RetrofitClient.api.get2025Series()

                // ДОБАВЬ ПРОВЕРКУ RESPONSE
                if (response.Response == "True") {
                    Log.d("ViewModel", "✅ Успех! Найдено сериалов: ${response.Search?.size ?: 0}")
                    serialsList.value = response.Search ?: emptyList()

                    // Логируем первые 3 сериала для проверки
                    response.Search?.take(3)?.forEach { serial ->
                        Log.d("ViewModel", "📺 Сериал: ${serial.Title}, Год: ${serial.Year}")
                    }
                } else {
                    Log.e("ViewModel", "❌ Ошибка API: ${response}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "💥 Ошибка загрузки: ${e.message}", e)
            }
        }
    }
}