package com.practicum.playlistmaker.basePlayList.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import kotlinx.coroutines.launch

abstract class BasePlayListViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    fun savePlayList(image: String?, nameOfAlbum: String?, descriptionOfAlbum: String?) {

        val data = PlayListData(
            playListId = 0,
            image,
            nameOfAlbum,
            descriptionOfAlbum,
            tracksId = emptyList(),
            countTracks = 0,
        )

        viewModelScope.launch {

            playListInteractor.addPlayList(data)

        }
    }
}