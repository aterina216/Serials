package com.example.serials

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.serials.ui.navigation.Navigation
import com.example.serials.ui.theme.SerialsTheme
import com.example.serials.ui.viewmodel.SerialsViewModel
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