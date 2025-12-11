package com.example.serials.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

@Composable
fun HomeScreen(
    viewModel: SerialsViewModel,
    controller: NavController
) {

    val serialList by viewModel._serialList.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()

    Log.d("HomeScreen", "🔄 Composable перерисовка. Количество сериалов: ${serialList.size}")

    Scaffold(
        topBar = {
            TopBar()
        }
    ) {

        PaddingValues ->
        if (serialList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(PaddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(text = "Загружаем сериалы")
            }
        } else {
            Column(
                modifier = Modifier.padding(PaddingValues)
                    .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Purple40.copy(alpha = 0.15f),
                            lightBlue.copy(alpha = 0.15f)
                        )
                    )
                )
            ) {
                Search("") { }
                LazyColumn {
                    items(serialList) { artist ->
                        SerialCard(artist, controller)
                    }
                }
            }
        }
    }
}