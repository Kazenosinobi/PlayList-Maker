package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.mediaLibrary.ui.favourite.FavouriteTracksViewModel
import com.practicum.playlistmaker.mediaLibrary.ui.playList.PlayListViewModel
import com.practicum.playlistmaker.playListBottomSheet.ui.PlayListBottomSheetViewModel
import com.practicum.playlistmaker.playListCreate.ui.PlayListCreateViewModel
import com.practicum.playlistmaker.playListScreen.ui.PlayListScreenViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MediaViewModel)
    viewModelOf(::FavouriteTracksViewModel)
    viewModelOf(::PlayListViewModel)
    viewModelOf(::PlayListCreateViewModel)
    viewModelOf(::PlayListBottomSheetViewModel)
    viewModelOf(::PlayListScreenViewModel)

}