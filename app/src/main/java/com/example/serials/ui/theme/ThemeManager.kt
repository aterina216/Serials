package com.example.serials.ui.theme

import android.content.Context
import android.content.SharedPreferences

class ThemeManager(context: Context) {

    companion object {
        const val PREF_NAME = "app_theme_prefs"
        const val KEY_THEME = "theme_mode"

        // Значения для темы
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveTheme(theme: String) {
        sharedPrefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun getTheme(): String {
        return sharedPrefs.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
    }
}