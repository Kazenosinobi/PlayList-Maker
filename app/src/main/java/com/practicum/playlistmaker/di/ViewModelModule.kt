package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.mediaLibrary.ui.FavouriteTracksViewModel
import com.practicum.playlistmaker.mediaLibrary.ui.PlayListViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MediaViewModel(get())
    }

    viewModel {
        FavouriteTracksViewModel()
    }

    viewModel {
        PlayListViewModel()
    }

}