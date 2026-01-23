package com.example.serials.utils

import android.content.Context

object StartScreenPreference {
    private const val PREFS_NAME = "start_screen_prefs"
    private const val KEY_START_SCREEN = "start_screen"

    fun getStartScreen(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_START_SCREEN, "Главная") ?: "Главная"
    }

    fun setStartScreen(context: Context, screen: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_START_SCREEN, screen)
            .apply()
    }
}