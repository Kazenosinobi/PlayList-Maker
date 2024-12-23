package com.practicum.playlistmaker.playListCreate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateInteractor
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.launch

class PlayListCreateViewModel(
    private val playListCreateInteractor: PlayListCreateInteractor,
) : ViewModel() {

    fun savePlayList(image: String?, nameOfAlbum: String?, descriptionOfAlbum: String?) {
        val data = PlayListCreateData(
            playListId = 0,
            image,
            nameOfAlbum,
            descriptionOfAlbum,
            tracks = emptyList()
        )
        viewModelScope.launch {
            playListCreateInteractor.addPlayListToFavouritePlayList(data)
        }
    }
}