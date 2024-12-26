package com.practicum.playlistmaker.basePlayList.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.basePlayList.domain.db.BasePlayListInteractor
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.launch

abstract class BasePlayListViewModel(
    private val basePlayListInteractor: BasePlayListInteractor
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
            basePlayListInteractor.addPlayListToFavouritePlayList(data)
        }
    }
}