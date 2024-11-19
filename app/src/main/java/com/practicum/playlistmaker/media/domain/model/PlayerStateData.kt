package com.practicum.playlistmaker.media.domain.model

data class PlayerStateData(
    val playerState: PlayerState,
    val currentPosition: String = DEFAULT_CURRENT_POS,
    val isFavourite: Boolean = false
) {
    private companion object {
        private const val DEFAULT_CURRENT_POS = "00:00"
    }
}


