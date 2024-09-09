package com.practicum.playlistmaker.core

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.playlistmaker.core.App.Companion.PREFERENCES
import com.practicum.playlistmaker.media.domain.api.MediaRepository
import com.practicum.playlistmaker.media.data.impl.MediaRepositoryImpl
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.impl.MediaInteractorImpl
import com.practicum.playlistmaker.mediaLibrary.domain.api.MediaLibraryRepository
import com.practicum.playlistmaker.mediaLibrary.data.impl.MediaLibraryRepositoryImpl
import com.practicum.playlistmaker.mediaLibrary.domain.api.MediaLibraryInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.impl.MediaLibraryInteractorImpl
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.localStorage.SearchHistory
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository
import com.practicum.playlistmaker.sharing.data.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun provideSettingsInteractor(application: App): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(application))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository(context))
    }

    fun provideMediaLibraryInteractor(): MediaLibraryInteractor {
        return MediaLibraryInteractorImpl(getMediaLibraryRepository())
    }

    fun provideMediaInteractor(): MediaInteractor {
        return MediaInteractorImpl(getMediaRepository())
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(), SearchHistory(
                context.getSharedPreferences(
                    PREFERENCES, MODE_PRIVATE
                )
            )
        )
    }

    private fun getSettingsRepository(application: App): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    private fun getSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    private fun getMediaLibraryRepository(): MediaLibraryRepository {
        return MediaLibraryRepositoryImpl()
    }

    private fun getMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }
}