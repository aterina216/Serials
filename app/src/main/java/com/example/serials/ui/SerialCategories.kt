package com.example.serials.ui

import androidx.appcompat.widget.DialogTitle
import androidx.room.Query

enum class SerialCategories(
    val title: String,
    val query: String
) {
    NEW("Новые", "new"),
    DRAMAS("Драмы", "drama"),
    CRIMINAL("Криминал", "criminal"),
    HORROR("Ужасы", "horror"),
    ACTION("Боевики", "action"),
    SCIENCE_FICTION("Научная фантастика", "sci-fi"),
    FANTASY("Фэнтези", "fantasy")
}