package com.example.serials.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.serials.ui.viewmodel.SerialsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.serials.R
import com.example.serials.ui.components.SerialCard
import com.example.serials.ui.theme.DarkViolet
import com.example.serials.ui.theme.Gray
import com.example.serials.ui.theme.LightGray
import com.example.serials.ui.theme.LightViolet
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import kotlin.math.exp

@Composable
fun HistoryScreen(viewModel: SerialsViewModel, navController: NavController) {

    val historySerials by viewModel.historySerials.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHistorySerials()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        DarkViolet.copy(alpha = 0.15f),
                        LightGray.copy(alpha = 0.15f)
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
                            colors = listOf(
                                LightViolet,
                                Gray
                            )
                        )
                    )
                    .shadow(elevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
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
                                        LightGray,
                                        LightViolet
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.time),
                            contentDescription = "История",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "История просмотров",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }


            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DarkViolet.copy(alpha = 0.95f),
                                Gray.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    LightGray.copy(alpha = 0.8f),
                                    LightViolet.copy(alpha = 0.8f),
                                    LightGray.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            }
            if (historySerials.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            LightViolet.copy(alpha = 0.4f),
                                            DarkViolet.copy(alpha = 0.15f)
                                        )
                                    )
                                )
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            LightGray,
                                            LightViolet
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .padding(28.dp),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Icon(
                                painter = painterResource(R.drawable.time),
                                contentDescription = "Нет истории",
                                modifier = Modifier.size(56.dp),
                                tint = LightViolet
                            )
                        }
                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "История просмотров пуста",
                            fontSize = 20.sp,
                            color = Gray,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Сериалы, которые вы посмотрите,\nпоявятся здесь",
                            fontSize = 16.sp,
                            color = Gray.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Просто откройте детали сериала,\nчтобы добавить его в историю",
                            fontSize = 14.sp,
                            color = Color(0xFF625b71).copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            end = 10.dp,
                            top = 10.dp,
                            bottom = 80.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(historySerials.sortedByDescending { it.watchedAt }) { serial ->
                            SerialCard(serial, navController)
                        }
                    }

                    if (historySerials.isNotEmpty()) {
                        Box(
                            modifier = Modifier.align(Alignment.BottomEnd)
                                .padding(24.dp, end = 24.dp)
                        ) {

                            Box(modifier = Modifier.size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Purple80.copy(alpha = 0.9f),
                                            DarkViolet.copy(alpha = 0.8f)
                                        )
                                    )
                                ).border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.8f),
                                            LightGray.copy(alpha = 0.6f)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .shadow(
                                    elevation = 12.dp,
                                    shape = CircleShape,
                                    ambientColor = Purple80.copy(alpha = 0.5f),
                                    spotColor = DarkViolet.copy(alpha = 0.3f)
                                )
                                .clickable {
                                    viewModel.clearHistory()
                                },
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Очистить историю",
                                    tint = Color.White,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}