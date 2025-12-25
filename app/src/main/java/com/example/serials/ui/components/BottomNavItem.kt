package com.example.serials.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.serials.R


sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val image: Int
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Домашняя страница",
        image = R.drawable.baseline_home_24
    )

    object Favorites: BottomNavItem(
        route = "favorites",
        title = "Избранное",
        image = R.drawable.baseline_favorite_24
    )

    object History: BottomNavItem(
        route = "history",
        title = "История",
        image = R.drawable.time
    )
}