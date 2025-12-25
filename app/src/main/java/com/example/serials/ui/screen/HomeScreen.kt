package com.example.serials.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.serials.ui.components.Search
import com.example.serials.ui.components.SerialCard
import com.example.serials.ui.components.TopBar
import com.example.serials.ui.theme.Pink40
import com.example.serials.ui.theme.Pink80
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.lightBlue
import com.example.serials.ui.viewmodel.SerialsViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: SerialsViewModel,
    controller: NavController
) {
    // Состояние строки поиска (должно быть здесь, а не в Search!)
    var searchText by remember { mutableStateOf("") }

    // Получаем данные
    val serialList by viewModel._serialList.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()

    // Решаем, какой список показывать
    val itemsToShow = if (searchText.isNotBlank()) {
        searchResult  // Показываем результаты поиска
    } else {
        serialList    // Показываем все сериалы
    }

    // Дебаунс для поиска (чтобы не спамить API на каждый символ)
    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            delay(500) // Ждем 500ms после последнего ввода
            viewModel.searchSerials(searchText)
        } else {
            // Если строка пустая - очищаем результаты поиска
            viewModel.searchSerials("")
        }
    }

    Log.d("HomeScreen", "Поиск: '$searchText', показываем: ${itemsToShow.size} элементов")

    Scaffold(
        topBar = {
            TopBar()
        }
    ) { paddingValues ->
        if (serialList.isEmpty() && searchText.isBlank()) {
            // Показываем загрузку только если нет сериалов И нет поиска
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(text = "Загружаем сериалы")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Purple40.copy(alpha = 0.15f),
                                lightBlue.copy(alpha = 0.15f)
                            )
                        )
                    )
            ) {
                Search(
                    searchText = searchText,
                    onTextChanged = { newText ->
                        searchText = newText
                    }
                )

                // Показываем состояние поиска
                when {
                    searchText.isNotBlank() && searchResult.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Outlined.Warning,
                                    contentDescription = "Не найдено",
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.Gray
                                )
                                Text("Не нашли \"$searchText\"", color = Color.Gray)
                            }
                        }
                    }
                    itemsToShow.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Нет сериалов для отображения", color = Color.Gray)
                        }
                    }
                    else -> {
                        LazyColumn {
                            items(itemsToShow) { serial ->
                                SerialCard(serial, controller)
                            }
                        }
                    }
                }
            }
        }
    }
}