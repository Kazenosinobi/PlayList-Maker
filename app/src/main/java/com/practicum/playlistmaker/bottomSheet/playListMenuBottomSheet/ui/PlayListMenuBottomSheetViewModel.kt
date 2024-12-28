package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import kotlinx.coroutines.launch

class PlayListMenuBottomSheetViewModel(
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    fun deletePlayList(playListId: Int) {
        viewModelScope.launch {
            playListInteractor.deletePlayList(playListId)
        }
    }

    fun sharePlayList(playList: PlayListData) {
        viewModelScope.launch {
            playListInteractor.share(playList)
        }
    }

}