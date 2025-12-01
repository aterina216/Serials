package com.example.serials.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serials.data.repository.SerialsRepository
import javax.inject.Inject

class ViewModelFactory (
    private val repository: SerialsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SerialsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SerialsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}