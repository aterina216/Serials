package com.example.serials.ui.theme

object GetThemeName {

    fun getThemeDisplayName(theme: String): String {
        return when(theme) {
            ThemeManager.THEME_LIGHT -> "Светлая"
                ThemeManager.THEME_DARK -> "Темная"
            ThemeManager.THEME_SYSTEM -> "Системная"
            else -> "Неизвестная тема"
        }
    }
}