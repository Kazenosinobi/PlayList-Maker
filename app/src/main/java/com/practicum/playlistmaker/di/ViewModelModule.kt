package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.mediaLibrary.ui.PlayListViewModel
import com.practicum.playlistmaker.mediaLibrary.ui.favourite.FavouriteTracksViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MediaViewModel)
    viewModelOf(::FavouriteTracksViewModel)
    viewModelOf(::PlayListViewModel)

}