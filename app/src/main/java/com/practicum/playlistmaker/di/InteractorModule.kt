package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.impl.MediaInteractorImpl
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.impl.FavouriteTracksInteractorImpl
import com.practicum.playlistmaker.mediaLibrary.domain.impl.PlayListInteractorImpl
import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateInteractor
import com.practicum.playlistmaker.playListCreate.domain.impl.PlayListCreateInteractorImpl
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenInteractor
import com.practicum.playlistmaker.playListScreen.domain.impl.PlayListScreenInteractorImpl
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val interactorModule = module {

    factory<ExecutorService> {
        Executors.newCachedThreadPool()
    }

    factoryOf(::TracksInteractorImpl).bind<TracksInteractor>()
    factoryOf(::SettingsInteractorImpl).bind<SettingsInteractor>()
    factoryOf(::SharingInteractorImpl).bind<SharingInteractor>()
    factoryOf(::MediaInteractorImpl).bind<MediaInteractor>()
    factoryOf(::FavouriteTracksInteractorImpl).bind<FavouriteTracksInteractor>()
    factoryOf(::PlayListCreateInteractorImpl).bind<PlayListCreateInteractor>()
    factoryOf(::PlayListInteractorImpl).bind<PlayListInteractor>()
    factoryOf(::PlayListScreenInteractorImpl).bind<PlayListScreenInteractor>()
}