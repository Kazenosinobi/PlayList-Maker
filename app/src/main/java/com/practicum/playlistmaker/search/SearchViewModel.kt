package com.practicum.playlistmaker.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.search.domain.models.Track

class SearchViewModel : ViewModel() {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel() as T
                }
        }
    }
}