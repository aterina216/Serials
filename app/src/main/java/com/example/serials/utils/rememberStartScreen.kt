package com.example.serials.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.setValue

@Composable
fun rememberStartScreen(): Pair<String, (String) -> Unit> {
    val context = LocalContext.current

    // Используем remember с обновлением состояния
    var startScreen by remember {
        mutableStateOf(StartScreenPreference.getStartScreen(context))
    }

    val setStartScreen: (String) -> Unit = { newScreen ->
        startScreen = newScreen
        StartScreenPreference.setStartScreen(context, newScreen)
    }

    return startScreen to setStartScreen
}