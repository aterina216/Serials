package com.example.serials.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.serials.ui.screen.HomeScreen
import com.example.serials.ui.viewmodel.SerialsViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.serials.ui.components.BottomBar
import com.example.serials.ui.screen.FavoritesScreen
import com.example.serials.ui.screen.HistoryScreen
import com.example.serials.ui.screen.RemindersScreen
import com.example.serials.ui.screen.SerialDetail
import com.example.serials.ui.screen.SettingsScreen
import com.example.serials.utils.StartScreenPreference

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModel: SerialsViewModel) {
    val navController = rememberNavController()
    val currenrDestination = navController.currentDestination

    val startScreenPref = StartScreenPreference.getStartScreen(LocalContext.current)
    val startDestination = when (startScreenPref) {
        "Избранное" -> "favorites"
        "История" -> "history"
        "Напоминания" -> "reminders"
        else -> "home"
    }

    // Логируем навигацию
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("NAV", "📍 Переход: ${destination.route}, args: $arguments")
        }
    }
    Scaffold(bottomBar = { BottomBar(navController, currenrDestination) })

    { PaddingValues->


        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(PaddingValues)
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
            composable("favorites") {
                FavoritesScreen(viewModel, navController)
            }
            composable("history") {
                HistoryScreen(viewModel, navController)
            }
            composable("reminders") {
                RemindersScreen(viewModel)
            }
            composable("settings") {
                SettingsScreen(navController, viewModel)
            }
        }
    }
}