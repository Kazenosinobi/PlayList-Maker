package com.practicum.playlistmaker.basePlayList.playListCreate.ui

import com.practicum.playlistmaker.basePlayList.domain.db.BasePlayListInteractor
import com.practicum.playlistmaker.basePlayList.ui.BasePlayListViewModel

class PlayListCreateViewModel(
    private val basePlayListInteractor: BasePlayListInteractor,
) : BasePlayListViewModel(basePlayListInteractor)