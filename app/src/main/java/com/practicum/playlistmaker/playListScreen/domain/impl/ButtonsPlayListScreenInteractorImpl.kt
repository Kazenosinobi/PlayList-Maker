package com.practicum.playlistmaker.playListScreen.domain.impl

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListScreen.domain.api.ButtonsPlayListScreenInteractor
import com.practicum.playlistmaker.playListScreen.domain.api.ButtonsPlayListScreenRepository

class ButtonsPlayListScreenInteractorImpl(private val repository: ButtonsPlayListScreenRepository) :
    ButtonsPlayListScreenInteractor {
    override fun share(playList: PlayListCreateData) {
        repository.share(playList)
    }
}