package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.domain.api.PlayListMenuBottomSheetInteractor
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.launch

class PlayListMenuBottomSheetViewModel(
    private val playListMenuBottomSheetInteractor: PlayListMenuBottomSheetInteractor,
) : ViewModel() {

    fun deletePlayList(playList: PlayListCreateData) {
        viewModelScope.launch {
            playListMenuBottomSheetInteractor.deletePlayListAtFavouritePlayList(playList)
        }
    }

    fun sharePlayList(playList: PlayListCreateData) {
        playListMenuBottomSheetInteractor.share(playList)
    }

}