package com.practicum.playlistmaker.search.domain.models

sealed class ViewState {
    class Success(trackList: List<Track>) : ViewState()
    object EmptyError : ViewState()
    object NetworkError : ViewState()
}