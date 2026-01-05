package com.example.serials.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import com.example.serials.R
import com.example.serials.ui.WatchStatus
import com.example.serials.ui.components.SerialCard
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.lightBlue
import com.example.serials.ui.viewmodel.SerialsViewModel


@Composable
fun FavoritesScreen(
    viewModel: SerialsViewModel,
    navController: NavController
) {
    var selectedIndex by remember { mutableStateOf(0) }

    // Используем ключи статусов, а не значения
    val statusKeys = WatchStatus.ALL_STATUSES
    val displayNames = WatchStatus.DISPLAY_NAMES

    val favoriteSerials by viewModel._favoriteSerials.collectAsState()


    LaunchedEffect(selectedIndex) {
        val statusKey = statusKeys[selectedIndex]
        viewModel.getFavoriteSerials(statusKey)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Purple40.copy(alpha = 0.15f),
                        lightBlue.copy(alpha = 0.15f)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Purple80, PurpleGrey40)
                        )
                    )
                    .shadow(elevation = 4.dp)
            ) {
                Text(
                    text = "Мои сериалы",
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Purple40.copy(alpha = 0.95f),
                                PurpleGrey40.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    lightBlue.copy(alpha = 0.8f),
                                    Purple80.copy(alpha = 0.8f),
                                    lightBlue.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    statusKeys.forEachIndexed {
                        index, statusKey ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (index == selectedIndex){
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        )
                                    }
                                    else {
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color.Transparent, Color.Transparent
                                            )
                                        )
                                    }
                                )
                                .border(
                                    width = if(index == selectedIndex) 1.5.dp else 0.dp,
                                    brush = if(index == selectedIndex)
                                        Brush.linearGradient(
                                            colors = listOf(lightBlue, Purple80)
                                        )
                                    else
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color.Transparent, Color.Transparent
                                            )
                                        ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedIndex = index
                                    viewModel.getFavoriteSerials(statusKey)
                                }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayNames[statusKey] ?: statusKey,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if(index == selectedIndex) FontWeight.Bold else FontWeight.SemiBold,
                                color = if(index == selectedIndex) Color.White else Color.White.copy(alpha = 0.8f),
                                maxLines = 1,
                                modifier = Modifier.shadow(
                                    elevation = if(index == selectedIndex) 2.dp else 0.dp,
                                    shape = RoundedCornerShape(4.dp)
                                )
                            )
                        }
                    }
                }
            }

            if(favoriteSerials.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Purple80.copy(alpha = 0.3f),
                                            Purple40.copy(alpha = 0.1f)
                                        )
                                    )
                                )
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(lightBlue, Purple80)
                                    ),
                                    shape = CircleShape
                                )
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.outline_star_24
                                ),
                                contentDescription = "Нет избранного",
                                modifier = Modifier.size(48.dp),
                                tint = Purple80
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = when(selectedIndex) {
                                0 -> "Вы еще не добавили сериалы\nв \"Хочу посмотреть\""
                                1 -> "Вы еще не начали смотреть\nни один сериал"
                                2 -> "Вы еще не посмотрели\nни один сериал"
                                else -> "Нет избранных сериалов"
                            },
                            fontSize = 16.sp,
                            color = PurpleGrey40,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = when(selectedIndex) {
                                0 -> "Найдите интересный сериал и добавьте его сюда"
                                1 -> "Начните смотреть сериал и он появится здесь"
                                2 -> "Отмечайте просмотренные сериалы в деталях"
                                else -> "Добавьте сериалы в избранное"
                            },
                            fontSize = 14.sp,
                            color = PurpleGrey40.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else {
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(favoriteSerials) {
                        serial -> SerialCard(serial, navController)
                    }
                }
            }
        }
    }
}