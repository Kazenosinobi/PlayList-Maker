package com.practicum.playlistmaker.media.ui

import com.practicum.playlistmaker.media.domain.model.PlayerState

data class PlayerStateData(
    val playerState: PlayerState = PlayerState.STATE_DEFAULT,
    val currentPosition: String = DEFAULT_CURRENT_POS,
    val isFavourite: Boolean = false
) {
    private companion object {
        private const val DEFAULT_CURRENT_POS = "00:00"
    }
}


