package com.example.serials.ui

object WatchStatus {

    const val WANT_TO_WATCH = "want_to_watch"
    const val WATCHING = "watching"
    const val WATCHED = "watched"

    val ALL_STATUSES = listOf(WANT_TO_WATCH, WATCHING, WATCHED)

    val DISPLAY_NAMES = mapOf(WANT_TO_WATCH to "Хочу посмотреть", WATCHING to "Смотрю", WATCHED to "Просмотрено")
}