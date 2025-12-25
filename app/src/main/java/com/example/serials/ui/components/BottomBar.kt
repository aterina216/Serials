package com.example.serials.ui.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.serials.ui.theme.Purple40
import com.example.serials.ui.theme.Purple80
import com.example.serials.ui.theme.PurpleGrey40
import com.example.serials.ui.theme.lightBlue

@Composable
fun BottomBar(
    navController: NavController,
    currenDestination: NavDestination?
) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.History
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                ambientColor = Purple80.copy(alpha = 0.3f),
                spotColor = Purple80.copy(alpha = 0.2f)
            ),
        color = Color.Transparent
    )
    {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Purple40.copy(alpha = 0.95f),
                            PurpleGrey40.copy(alpha = 0.9f)
                        )
                    )
                ).clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        )
        {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align (Alignment.TopCenter)
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

            BottomNavigation(
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                elevation = 0.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                items.forEach { item ->
                    val selected =
                        currenDestination?.hierarchy?.any { it.route == item.route } == true

                    BottomNavigationItem(
                        icon = {
                            Box(
                                modifier = Modifier.size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if(selected) {
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.3f),
                                                    Color.Transparent
                                                )
                                            )
                                        } else {
                                            Brush.linearGradient(
                                                colors = listOf(Color.Transparent,
                                                    Color.Transparent)
                                            )
                                        }
                                    )
                                    .border(
                                        width = if (selected) 1.5.dp else 0.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(lightBlue, Purple80)
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(item.image),
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = if(selected) Color.White else Color.White.copy(
                                        alpha = 0.8f
                                    )
                                )
                            }
                            },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (selected) Color.White else Color.White.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        selected = selected,
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.8f),
                        alwaysShowLabel = true
                    )
                }
            }
        }
    }
}