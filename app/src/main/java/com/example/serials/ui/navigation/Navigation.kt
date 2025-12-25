package com.example.serials.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import com.example.serials.ui.screen.SerialDetail

@Composable
fun Navigation(viewModel: SerialsViewModel) {
    val navController = rememberNavController()
    val currenrDestination = navController.currentDestination

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
            startDestination = "home",
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
                FavoritesScreen()
            }
            composable("history") {
                HistoryScreen()
            }
        }
    }
}