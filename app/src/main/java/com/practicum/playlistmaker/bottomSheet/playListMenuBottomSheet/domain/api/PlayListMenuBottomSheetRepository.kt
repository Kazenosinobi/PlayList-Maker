package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.domain.api

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

interface PlayListMenuBottomSheetRepository {
    fun share(playList: PlayListCreateData)
    suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData)
}