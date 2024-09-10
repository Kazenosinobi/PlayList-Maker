package com.practicum.playlistmaker.search.domain.models

sealed class ViewState {
    class Success(val trackList: List<Track>) : ViewState()
    class History(val historyList: List<Track>) : ViewState()
    data object EmptyError : ViewState()
    data object NetworkError : ViewState()
    data object Loading : ViewState()
}