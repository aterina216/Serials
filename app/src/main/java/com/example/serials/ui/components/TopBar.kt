package com.example.serials.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.Violet

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Минимальная высота
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Purple80, PurpleGrey40)
                )
            )
            .shadow(elevation = 4.dp)
    ) {
        Text(
            text = "Сериальчики",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 25.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}