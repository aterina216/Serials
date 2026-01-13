package com.example.serials.ui.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.serials.R
import com.example.serials.data.mapper.convertSerialEntityFromDetails
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.ui.components.DateDialog
import com.example.serials.ui.components.DetailRow
import com.example.serials.ui.components.StatusDropdownButton
import com.example.serials.ui.components.TimeDialog
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.viewmodel.SerialsViewModel
import com.example.serials.utils.DownloadCover.downloadCover
import com.example.serials.utils.Reminder.showReminder
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SerialDetail(
    viewModel: SerialsViewModel,
    navController: NavController,
    imdbID: String
) {
    Log.d("DEBUG", "🔵 SerialDetail вызван, imdbID: $imdbID")

    // УБИРАЕМ сразу разыменование!!
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val serialDetail by viewModel.currentserial.collectAsState()
    val currentStatus by viewModel._currentStatus.collectAsState()


    LaunchedEffect(currentStatus) {
        Log.d("DEBUG_UI", "Текущий статус обновился: $currentStatus")
    }

    LaunchedEffect(imdbID) {
        Log.d("DEBUG_UI", "Загрузка деталей для: $imdbID")
        delay(1000)

        if (imdbID.isNotBlank() && imdbID != "null") {
            try {
                viewModel.loadSerialDetails(imdbID)
                viewModel.loadSerialStatus(imdbID)
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки: ${e.message}"
                Log.e("DEBUG_UI", "💥 Ошибка загрузки", e)
            }
        } else {
            errorMessage = "ID сериала не указан"
        }
        isLoading = false
    }


    // UI в зависимости от состояния
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Загружаем...")
                }
            }
        }

        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Ошибка",
                        modifier = Modifier.size(48.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = errorMessage ?: "Произошла ошибка")
                }
            }
        }

        serialDetail == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Нет данных",
                        modifier = Modifier.size(48.dp),
                        tint = Color.Yellow
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Данные о сериале не найдены")
                    Text("ID: $imdbID", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        else -> {
            // ТОЛЬКО ТЕПЕРЬ показываем данные
            ShowSerialDetails(serialDetail!!, viewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSerialDetails(
    details: SerialDetails,
    viewModel: SerialsViewModel
) {
    Log.d("DEBUG_UI", "ShowSerialDetails для: ${details.Title}")

    LaunchedEffect(details.imdbID) {
        Log.d("DEBUG_UI", "Загрузка статуса для: ${details.imdbID}")
        viewModel.loadSerialStatus(details.imdbID)
    }

    val currentStatus by viewModel._currentStatus.collectAsState()
    val scrollState = rememberScrollState()
    var fabVisible by remember { mutableStateOf(true) }
    var previosScroll by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var showDateDialog by remember { mutableStateOf(false) }
    var date: Long? by remember { mutableStateOf(0) }
    var showTime by remember { mutableStateOf(false) }
    var time: Long? by remember { mutableStateOf(0) }
    val calendarVisible by remember(currentStatus) {
        derivedStateOf { currentStatus == "want_to_watch" }
    }

    LaunchedEffect(scrollState.value) {
        val currentScroll = scrollState.value
        val scrollingDown = currentScroll > previosScroll

        if (scrollingDown && currentScroll > 50) {
            fabVisible = false
        } else if (!scrollingDown) {
            fabVisible = true
        }
        previosScroll = currentScroll
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Постер
            details.Poster?.let { poster ->
                AsyncImage(
                    model = poster,
                    contentDescription = details.Title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                val entity = convertSerialEntityFromDetails(details)
                entity.watchedAt = System.currentTimeMillis()

                LaunchedEffect(entity) {
                    viewModel.updateCurrentTime(entity.watchedAt, entity.imdbID)
                }

                StatusDropdownButton(
                    currentStatus = currentStatus,
                    onStatusSelected = { newStatus ->
                        Log.d(
                            "DEBUG_UI",
                            "Пользователь выбрал статус: $newStatus для ${details.imdbID}"
                        )
                        viewModel.updateSerialStatus(
                            entity.imdbID,
                            newStatus
                        )
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                )

            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Gray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет постера", color = Color.Gray)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Заголовок и рейтинг
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = details.Title ?: "Без названия",
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    details.imdbRating?.let { rating ->
                        Badge(
                            containerColor = MaterialTheme.colors.primary,
                            content = {
                                Text(rating, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        )
                    }
                }

                // Мета-информация
                Text(
                    text = buildString {
                        details.Year?.let { append("$it • ") }
                        details.Runtime?.takeIf { it != "N/A" }?.let { append("$it • ") }
                        details.Genre?.takeIf { it != "N/A" }?.let { append(it) }
                        // Убираем последний " • " если он есть
                        if (endsWith(" • ")) delete(length - 3, length)
                    },
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Сюжет
                details.Plot?.takeIf { it != "N/A" && it.isNotEmpty() }?.let { plot ->
                    Text(
                        text = plot,
                        style = MaterialTheme.typography.body2,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Все детали
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    details.Director?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Режиссер", it)
                    }

                    details.Writer?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Сценарист", it)
                    }

                    details.Actors?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Актеры", it)
                    }

                    details.Country?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Страна", it)
                    }

                    details.Language?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Язык", it)
                    }

                    details.Awards?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Награды", it)
                    }

                    if (details.Type == "series") {
                        details.totalSeasons?.takeIf { it.isNotEmpty() }?.let {
                            DetailRow("Сезонов", it)
                        }
                    }

                    details.Rated?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Рейтинг", it)
                    }

                    details.Released?.takeIf { it != "N/A" && it.isNotEmpty() }?.let {
                        DetailRow("Дата выхода", it)
                    }

                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {

        AnimatedVisibility(
            visible = fabVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ExtendedFloatingActionButton(
                    onClick = {
                        Log.d("Download", "Скачивание постера для: ${details.Title}")
                        downloadCover(
                            context = context,
                            imageUrl = details.Poster,
                            title = details.Title
                        )
                        Toast.makeText(
                            context,
                            "Начинаем скачивать постер...",
                            Toast.LENGTH_SHORT
                        )
                            .show() // Toast.makeText(context, "Начинаем скачивать постер...", Toast.LENGTH_SHORT).show(")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_save_24),
                            contentDescription = "Скачать постер",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    text = {
                        Text(
                            "Скачать постер",
                            style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    containerColor = Purple80.copy(alpha = 0.9f),
                    contentColor = Color.White,
                    modifier = Modifier.shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Purple80.copy(alpha = 0.5f),
                        spotColor = Purple40.copy(alpha = 0.3f)
                    )
                )

                if (calendarVisible) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            showDateDialog = true
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.outline_calendar_month_24),
                                contentDescription = "Установить напоминание",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        text = {
                            Text(
                                "Установить напоминание",
                                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        containerColor = Purple80.copy(alpha = 0.9f),
                        contentColor = Color.White,
                        modifier = Modifier.shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Purple80.copy(alpha = 0.5f),
                            spotColor = Purple40.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
        if (showDateDialog) {
            DateDialog(
                onDateSelected = {
                    date = it
                    Log.d("DEBUG", "Выбрана дата: $date")
                    showDateDialog = false
                    showTime = true
                },
                onDismiss = {
                    showDateDialog = false
                }
            )
        }
        if (showTime) {
            TimeDialog(
                selectedDateMillis = date!!,
                onTimeSelected = {
                    time = it
                    Log.d("DEBUG", "Выбрано время: $time")
                    showReminder(
                        context,
                        serialDetails = details,
                        time = time!!,
                    )
                },
                onDismiss = { showTime = false }
            )
        }
    }
}