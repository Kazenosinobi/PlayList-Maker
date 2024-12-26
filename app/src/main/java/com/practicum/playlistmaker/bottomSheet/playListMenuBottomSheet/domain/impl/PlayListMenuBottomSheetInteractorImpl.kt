package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.domain.impl

import com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.domain.api.PlayListMenuBottomSheetInteractor
import com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.domain.api.PlayListMenuBottomSheetRepository
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData

class PlayListMenuBottomSheetInteractorImpl(private val repository: PlayListMenuBottomSheetRepository) :
    PlayListMenuBottomSheetInteractor {
    override fun share(playList: PlayListCreateData) {
        repository.share(playList)
    }

    override suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData) {
        repository.deletePlayListAtFavouritePlayList(playList)
    }


}