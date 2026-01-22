package com.example.serials.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serials.data.repository.SerialsRepository
import com.example.serials.ui.theme.ThemeManager
import javax.inject.Inject

class ViewModelFactory (
    private val repository: SerialsRepository,
    private val themeManager: ThemeManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SerialsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SerialsViewModel(repository, themeManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}