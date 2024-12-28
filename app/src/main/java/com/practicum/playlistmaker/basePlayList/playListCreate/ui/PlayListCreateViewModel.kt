package com.practicum.playlistmaker.basePlayList.playListCreate.ui

import com.practicum.playlistmaker.basePlayList.ui.BasePlayListViewModel
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor

class PlayListCreateViewModel(
    playListInteractor: PlayListInteractor,
) : BasePlayListViewModel(playListInteractor)