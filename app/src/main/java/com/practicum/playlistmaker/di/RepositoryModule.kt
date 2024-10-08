package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.media.data.impl.MediaRepositoryImpl
import com.practicum.playlistmaker.media.domain.api.MediaRepository
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidApplication() as App)
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidApplication())
    }

    single<MediaRepository> {
        MediaRepositoryImpl(get())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

}