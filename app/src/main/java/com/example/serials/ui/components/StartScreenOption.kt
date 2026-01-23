package com.example.serials.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serials.R
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.lightBlue
import com.example.serials.utils.rememberStartScreen
import kotlin.math.exp

@Composable
fun StartScreenOption() {

    val context = LocalContext.current
    val (selectedStartScreen, setStartScreen) = rememberStartScreen()

    var expanded by remember {
        mutableStateOf(false)
    }

    val startScreenOptions = listOf(
        StartScreen("Главная", "Домашняя страница с сериалами", R.drawable.baseline_home_24),
        StartScreen("Избранное", "Мои сохраненные сериалы", R.drawable.outline_star_24),
        StartScreen("История", "Недавно просмотренные сериалы", R.drawable.time),
        StartScreen("Напоминания", "Мои установленные напоминания", R.drawable.baseline_access_alarm_24)
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center) {

        Box(modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        lightBlue, Purple80
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_home_24),
                contentDescription = "Стартовый экран",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Стартовый экран",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Purple80.copy(alpha = 0.1f),
                    lightBlue.copy(alpha = 0.05f)
                )
            )
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Purple80.copy(alpha = 0.3f),
                    lightBlue.copy(alpha = 0.3f)
                )
            ), shape = RoundedCornerShape(16.dp)
        )
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Purple80.copy(alpha = 0.2f),
            spotColor = Purple80.copy(alpha = 0.1f)
        )
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Purple80.copy(alpha = 0.1f),
                            lightBlue.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Purple80.copy(alpha = 0.2f),
                            lightBlue.copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { expanded = true }
                .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Текущий стартовый экран",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.6f
                            ))

                        Text(
                            text = selectedStartScreen,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = "Выбрать стартовый экран",
                        tint = Purple80
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "При следующем запуске приложение откроется на выбранном экране",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                lineHeight = 18.sp
            )

            Box{
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {expanded = false},
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.95f),
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                                )
                            )
                        )
                ) {
                    startScreenOptions.forEach {
                        option ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(option.iconRes),
                                        contentDescription = null,
                                        tint = Purple80,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = option.title,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            text = option.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.6f
                                            )
                                        )
                                    }
                                }
                            },
                            onClick = {
                                setStartScreen(option.title)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }

}