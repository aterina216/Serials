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
import com.example.serials.ui.SerialCategories
import com.example.serials.ui.components.SerialCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch

class SerialsViewModel(private val repository: SerialsRepository): ViewModel() {

    private var serialsList = MutableStateFlow<List<SerialEntity>>(emptyList())
    val _serialList: StateFlow<List<SerialEntity>> = serialsList.asStateFlow()

    var currentserial = MutableStateFlow<SerialDetails?>(null)

    private var _searchResult = MutableStateFlow<List<SerialEntity>>(emptyList())
    val searchResult: StateFlow<List<SerialEntity>> = _searchResult

    private var currentCategory = MutableStateFlow<SerialCategories>(SerialCategories.NEW)
    val _currntCategory: StateFlow<SerialCategories> = currentCategory

    var currentPage = MutableStateFlow<Int>(1)
    private var _hasMore = MutableStateFlow<Boolean>(true)
    val hasMore: StateFlow<Boolean> = _hasMore
    private var _isLoading  = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var currentSearchPage = MutableStateFlow<Int>(1)
    private var _hasMoreSearch = MutableStateFlow<Boolean>(true)
    val hasMoreSearch: StateFlow<Boolean> = _hasMoreSearch

    private var _isLoadingSearch = MutableStateFlow(false)
    val isLoadingSearch: StateFlow<Boolean> = _isLoadingSearch
    private var _currentSearchCategory = MutableStateFlow("")


    init {
        Log.d("ViewModel", "🚀 ViewModel создан")
        loadSerialsFromCategory(currentCategory.value)
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


    fun searchSerials (query: String, loadMore: Boolean = false) {
        Log.d("SEARCH", "searchSerials: query='$query', loadMore=$loadMore, page=${currentSearchPage.value}")

        if(!loadMore || query != _currentSearchCategory.value) {
            currentSearchPage.value = 1
            _hasMoreSearch.value = true
            _searchResult.value = emptyList()
            _currentSearchCategory.value = query
            Log.d("SEARCH", "Новый поиск или запрос изменился, сброс пагинации")
        }

        if(!_hasMoreSearch.value || _isLoadingSearch.value) {
            Log.d("SEARCH", "❌ Не загружаем: hasMore=${_hasMoreSearch.value}, isLoading=${_isLoadingSearch.value}")
            return
        }
        _isLoadingSearch.value = true

        viewModelScope.launch {

            try {
                val page = if (loadMore) currentSearchPage.value else 1
                Log.d("SEARCH", "Загружаем страницу $page для запроса '$query'")

                val result = repository.searchSeries(query, page)
                Log.d("SEARCH", "Получено ${result.size} результатов")

                if(result.isNotEmpty()) {
                    if(loadMore) {
                        _searchResult.value = _searchResult.value + result
                    }
                    else {
                        _searchResult.value = result
                    }
                    currentSearchPage.value = page + 1
                    _hasMoreSearch.value = result.size == 10
                    Log.d("SEARCH", "Успех! Новая страница: ${currentSearchPage.value}, hasMore=${_hasMoreSearch.value}")
                }
                else {
                    _hasMoreSearch.value = false
                    Log.d("SEARCH", "Пустой результат, hasMore=false")
                }
            }
            catch (e: Exception) {
                Log.e("SEARCH", "Ошибка поиска: ${e.message}")
                _hasMoreSearch.value = false
            } finally {
                _isLoadingSearch.value = false
            }
        }

    }

    fun loadSerialsFromCategory(category: SerialCategories, resetPagination: Boolean = true) {
        if(_isLoading.value) return

        currentCategory.value = category

        if(resetPagination) {
            currentPage.value = 1
            _hasMore.value = true
            serialsList.value = emptyList()
        }
        loadNextPage()
    }

    fun loadNextPage() {
        Log.d("PAGINATION", "loadNextPage: hasMore=$_hasMore.value, isLoading=$_isLoading.value, page=${currentPage.value}")

        if(!_hasMore.value || _isLoading.value){
            Log.d("PAGINATION", "❌ Не загружаем: hasMore=${_hasMore.value}, isLoading=${_isLoading.value}")
            return }
        _isLoading.value = true
        Log.d("PAGINATION", "🚀 Начинаем загрузку страницы ${currentPage.value}")

        viewModelScope.launch {
            try {
                val category = currentCategory.value
                val page = currentPage.value

                val result = when (category) {
                    SerialCategories.NEW -> repository.loadSerialsFromApi(page)
                    else -> {
                        val query = category.query
                        repository.loadSerialsFromCategories(query, page)

                    }
                }
                var loadingBooks = result
                serialsList.value += loadingBooks

                currentPage.value = page + 1

                _hasMore.value = result.size == 10 && page <= 10
            }
            catch (e: Exception) {
                Log.e("ViewModel", "Ошибка загрузки сериалов: ${e.message}")
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}