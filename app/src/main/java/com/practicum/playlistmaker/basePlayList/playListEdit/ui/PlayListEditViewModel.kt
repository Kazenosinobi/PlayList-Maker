package com.practicum.playlistmaker.basePlayList.playListEdit.ui

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.basePlayList.domain.db.BasePlayListInteractor
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import com.practicum.playlistmaker.basePlayList.ui.BasePlayListViewModel
import com.practicum.playlistmaker.playListScreen.ui.PlayListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlayListEditViewModel(
    private val playListId: Int,
    private val basePlayListInteractor: BasePlayListInteractor,
) : BasePlayListViewModel(basePlayListInteractor) {

    private var playList: PlayListCreateData? = null

    private val playListEditStateFlow =
        MutableStateFlow<PlayListScreenState>(PlayListScreenState.Empty)

    fun getPlayListEditStateFlow() = playListEditStateFlow.asStateFlow()

    init {
        loadPlayList()
    }

    fun updatePlayList(
        image: String?,
        nameOfAlbum: String?,
        descriptionOfAlbum: String?
    ) {
        viewModelScope.launch {
            editPlayList(
                image,
                nameOfAlbum,
                descriptionOfAlbum
            )?.let { playList ->
                basePlayListInteractor.updateFavouritePlayList(playList)
            }
        }
    }

    private fun loadPlayList() {
        basePlayListInteractor.getPlayListById(playListId)
            .onEach { playList ->
                playListEditStateFlow.emit(PlayListScreenState.Content(playList))
                this.playList = playList
            }
            .launchIn(viewModelScope)
    }

    private fun editPlayList(
        image: String?,
        nameOfAlbum: String?,
        descriptionOfAlbum: String?
    ): PlayListCreateData? {

        return playList?.copy(
            image = image,
            nameOfAlbum = nameOfAlbum,
            descriptionOfAlbum = descriptionOfAlbum
        )

    }
}