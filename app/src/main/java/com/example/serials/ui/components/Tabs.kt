package com.example.serials.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.serials.ui.SerialCategories
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.lightBlue


@Composable
fun Tabs(
    categories: List<SerialCategories>,
    selectedCategory: SerialCategories,
    onCategorySelected: (SerialCategories) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier
        .fillMaxWidth()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Purple40.copy(alpha = 0.95f),
                    PurpleGrey40.copy(alpha = 0.9f)
                )
            )
        ).clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    )
     {
        Box (modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        lightBlue.copy(alpha = 0.8f),
                        Purple80.copy(alpha = 0.8f),
                        lightBlue.copy(alpha = 0.8f)
                    )
                )
            ))
        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp))
        {
            categories.forEach {
                category ->
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (category == selectedCategory) {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        }
                        else {
                            Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        }
                    )
                    .border(
                        width = if (category == selectedCategory) 1.5.dp else 0.dp,
                        brush = if (category == selectedCategory) Brush.linearGradient(
                            colors = listOf(lightBlue, Purple80)
                        ) else Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.Transparent)
                        ),
                        shape = RoundedCornerShape(20.dp)
                        )
                    .clickable  {onCategorySelected(category)}
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                    )
                {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (category == selectedCategory) Color.White else Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        modifier = Modifier.shadow(
                            elevation = if (category == selectedCategory) 2.dp else 0.dp,
                            shape = RoundedCornerShape(4.dp)
                        )
                    )
                }
            }
        }
    }
}