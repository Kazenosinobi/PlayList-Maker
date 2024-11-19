package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.media.data.impl.MediaRepositoryImpl
import com.practicum.playlistmaker.media.domain.api.MediaRepository
import com.practicum.playlistmaker.mediaLibrary.data.impl.FavouriteTracksRepositoryImpl
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksRepository
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::TracksRepositoryImpl).bind<TracksRepository>()

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidApplication() as App)
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidApplication())
    }

    factoryOf(::MediaRepositoryImpl).bind<MediaRepository>()

    factory<MediaPlayer> {
        MediaPlayer()
    }

    singleOf(::FavouriteTracksRepositoryImpl).bind<FavouriteTracksRepository>()

}