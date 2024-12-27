package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import kotlinx.coroutines.launch

class PlayListMenuBottomSheetViewModel(
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    fun deletePlayList(playList: PlayListData) {
        viewModelScope.launch {
            playListInteractor.deletePlayList(playList)
        }
    }

    fun sharePlayList(playList: PlayListData) {
        playListInteractor.share(playList)
    }

}