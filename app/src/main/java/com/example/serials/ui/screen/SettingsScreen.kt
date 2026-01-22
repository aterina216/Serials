package com.example.serials.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serials.R
import com.example.serials.ui.components.InfoRow
import com.example.serials.ui.components.ThemeOption
import com.example.serials.ui.theme.DarkGray
import com.example.serials.ui.theme.DarkViolet
import com.example.serials.ui.theme.GetThemeName.getThemeDisplayName
import com.example.serials.ui.theme.Gray
import com.example.serials.ui.theme.LightGray
import com.example.serials.ui.theme.LightViolet
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.ThemeManager
import com.example.serials.ui.theme.lightBlue
import com.example.serials.ui.viewmodel.SerialsViewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.serials.ui.components.ReminderFlag
import com.example.serials.utils.Reminder.cancelReminder
import com.example.serials.utils.Reminder.clearAllReminders


@Composable
fun SettingsScreen(navController: NavController,
                   viewModel: SerialsViewModel) {

    val currentTheme by viewModel.currentTheme.collectAsState()
    val context = LocalContext.current
    val reminders by viewModel.reminders.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Purple40.copy(alpha = 0.15f),
                        lightBlue.copy(alpha = 0.15f)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Purple80, PurpleGrey40
                        )
                    )
                )
                .shadow(elevation = 4.dp)
        ){
            Text(
                text = "Настройки",
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

        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        ReminderFlag(
            onDisableReminders = {

                    clearAllReminders(context, reminders)
                    viewModel.deleteAllReminders()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier
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
                            colors = listOf(lightBlue, Purple80)
                        ),
                        shape = CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_palette_24),
                        contentDescription = "Тема",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(text = "Внешний вид",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LightViolet.copy(alpha = 0.3f),
                            Purple80.copy(alpha = 0.1f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LightGray.copy(alpha = 0.5f),
                            LightViolet.copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Purple80.copy(alpha = 0.3f),
                    spotColor = Purple80.copy(alpha = 0.2f)
                ).padding(16.dp)
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ThemeOption(
                        icon = painterResource(R.drawable.baseline_sunny_24),
                        title = "Светлая",
                        subTitle = "Светлая цветовая схема",
                        isSelected = currentTheme == ThemeManager.THEME_LIGHT,
                        gradientColors = listOf(
                            LightViolet.copy(alpha = 0.7f),
                            lightBlue.copy(alpha = 0.5f)
                        ),
                        onClick = {viewModel.changeTheme(ThemeManager.THEME_LIGHT)}
                    )

                    ThemeOption(
                        icon = painterResource(R.drawable.outline_moon_stars_24),
                        title = "Темная",
                        subTitle = "Темная цветовая схема",
                        isSelected = currentTheme == ThemeManager.THEME_DARK,
                        gradientColors = listOf(
                            DarkViolet.copy(alpha = 0.7f),
                            Purple80.copy(alpha = 0.5f)
                        ),
                        onClick = {viewModel.changeTheme(ThemeManager.THEME_DARK)}
                    )

                    ThemeOption(
                        icon = painterResource(R.drawable.outline_settings_24),
                        title = "Системная",
                        subTitle = "Системная цветовая схема",
                        isSelected = currentTheme == ThemeManager.THEME_SYSTEM,
                        gradientColors = listOf(
                            Purple80.copy(alpha = 0.7f),
                            LightViolet.copy(alpha = 0.5f)
                        ),
                        onClick = {viewModel.changeTheme(ThemeManager.THEME_SYSTEM)}
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier
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
                                lightBlue, Purple80
                            )
                        ),
                        shape = CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "О приложении",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "О приложении",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                LightViolet.copy(alpha = 0.3f),
                                Purple80.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                LightGray.copy(alpha = 0.5f),
                                LightViolet.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Purple80.copy(alpha = 0.3f),
                        spotColor = Purple80.copy(alpha = 0.2f)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Сериальчики",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGray
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Purple80.copy(alpha = 0.3f),
                                            Purple40.copy(alpha = 0.1f)
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            lightBlue, Purple80
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "v1.0.0",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Purple80
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Приложение для просмотра сериалов",
                        fontSize = 14.sp,
                        color = Gray,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Находите новые сериалы, добавляйте в избранное и многое другое!",
                        fontSize = 14.sp,
                        color = Gray.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InfoRow(
                            icon = painterResource(R.drawable.baseline_sunny_24),
                            text = "Тема: ${getThemeDisplayName(currentTheme)}",
                            color = Purple80
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}