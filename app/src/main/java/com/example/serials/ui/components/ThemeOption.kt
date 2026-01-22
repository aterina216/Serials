package com.example.serials.ui.components


import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serials.ui.theme.DarkGray
import com.example.serials.ui.theme.Gray
import com.example.serials.ui.theme.LightGray
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.lightBlue

@Composable
fun ThemeOption(
    icon: Painter,
    title: String,
    subTitle: String,
    isSelected: Boolean,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    Brush.linearGradient(
                        colors = gradientColors
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent.copy(alpha = 0.1f)
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                brush = if (isSelected) {
                    Brush.linearGradient(
                        colors = listOf(
                            lightBlue,
                            Purple80
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            LightGray.copy(alpha = 0.3f),
                            LightGray.copy(alpha = 0.1f)
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
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
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(lightBlue, Purple80)
                    ),
                    shape = CircleShape
                )
                .shadow(
                    elevation = if(isSelected) 4.dp else 2.dp,
                    shape = CircleShape,
                    ambientColor = Purple80.copy(alpha = 0.3f),
                    spotColor = Purple80.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = if(isSelected) Color.White else Purple80,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if(isSelected) Color.White else DarkGray
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subTitle,
                fontSize = 13.sp,
                color = if(isSelected) Color.White.copy(alpha = 0.9f) else Gray
            )
        }

        if(isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White, lightBlue
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
                        shape = CircleShape
                    )
                    .shadow(elevation = 2.dp, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Purple80))
            }
        }
    }
}