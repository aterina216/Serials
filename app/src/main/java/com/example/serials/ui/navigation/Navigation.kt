package com.example.serials.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.serials.ui.screen.HomeScreen
import com.example.serials.ui.viewmodel.SerialsViewModel
import androidx.navigation.compose.composable

@Composable
fun Navigation(viewModel: SerialsViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"  // ✅ Указываем строку для стартового экрана
    ) {
        // ✅ Внутри блока builder определяем экраны
        composable("home") {
            HomeScreen(viewModel = viewModel)
        }

        // Можно добавить другие экраны:
        // composable("details/{id}") { ... }
        // composable("favorites") { ... }
    }
}