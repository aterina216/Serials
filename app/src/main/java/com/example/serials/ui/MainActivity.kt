package com.example.serials.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.serials.ui.screen.HomeScreen
import com.example.serials.ui.theme.SerialsTheme
import com.example.serials.ui.viewmodel.SerialsViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = SerialsViewModel()

        enableEdgeToEdge()
        setContent {
            SerialsTheme {
                HomeScreen(viewModel)
            }
        }
    }
}