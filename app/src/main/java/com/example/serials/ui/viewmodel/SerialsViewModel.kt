package com.example.serials.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serials.data.db.entity.SerialEntity
import com.example.serials.data.remote.api.RetrofitClient
import com.example.serials.data.remote.dto.SerialOMDb
import com.example.serials.data.repository.SerialsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SerialsViewModel(private val repository: SerialsRepository): ViewModel() {

    private var serialsList = MutableStateFlow<List<SerialEntity>>(emptyList())
    val _serialList: StateFlow<List<SerialEntity>> = serialsList.asStateFlow()

    init {
        Log.d("ViewModel", "🚀 ViewModel создан")
        loadSerialsFromDB()
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
}