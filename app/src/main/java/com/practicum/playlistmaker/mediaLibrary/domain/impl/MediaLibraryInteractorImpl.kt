package com.practicum.playlistmaker.mediaLibrary.domain.impl

import com.practicum.playlistmaker.mediaLibrary.domain.api.MediaLibraryInteractor
import com.practicum.playlistmaker.mediaLibrary.data.api.MediaLibraryRepository

class MediaLibraryInteractorImpl(private val repository: MediaLibraryRepository) : MediaLibraryInteractor {
}