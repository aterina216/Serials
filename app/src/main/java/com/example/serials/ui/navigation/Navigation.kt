package com.example.serials.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.serials.ui.screen.HomeScreen
import com.example.serials.ui.viewmodel.SerialsViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.serials.ui.screen.SerialDetail

@Composable
fun Navigation(viewModel: SerialsViewModel) {
    val navController = rememberNavController()

    // Логируем навигацию
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("NAV", "📍 Переход: ${destination.route}, args: $arguments")
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(viewModel = viewModel, navController)
        }

        composable(
            "details/{imdbID}",
            arguments = listOf(navArgument("imdbID") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val imdbID = navBackStackEntry.arguments?.getString("imdbID") ?: ""
            Log.d("NAV", "🎬 Открываем детали, imdbID: '$imdbID'")

            SerialDetail(
                viewModel = viewModel,
                navController = navController,
                imdbID = imdbID
            )
        }
    }
}