package com.example.serials.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serials.SerialsApp
import com.example.serials.ui.navigation.Navigation
import com.example.serials.ui.screen.HomeScreen
import com.example.serials.ui.theme.SerialsTheme
import com.example.serials.ui.viewmodel.SerialsViewModel
import com.example.serials.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: SerialsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "🎬 Activity создается")

        (application as SerialsApp).appComponent.inject(this)
        Log.d("MainActivity", "💉 DI завершен")


        enableEdgeToEdge()
        setContent {
            SerialsTheme {

                val viewModel: SerialsViewModel = viewModel
                Navigation(viewModel)
            }
        }
    }
}