package com.example.serials.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.serials.ui.components.SerialCard
import com.example.serials.ui.viewmodel.SerialsViewModel

@Composable
fun HomeScreen(
    viewModel: SerialsViewModel
) {

    val serialList by viewModel._serialList.collectAsState()

    Log.d("HomeScreen", "🔄 Composable перерисовка. Количество сериалов: ${serialList.size}")


    if (serialList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(text = "Загружаем сериалы")
        }
    } else {
        LazyColumn {
            items(serialList) { artist ->
                SerialCard(artist)
            }
        }
    }
}