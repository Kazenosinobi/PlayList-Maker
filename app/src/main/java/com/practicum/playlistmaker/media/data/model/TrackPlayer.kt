package com.practicum.playlistmaker.media.data.model

interface TrackPlayer {
    fun play(trackId: Int, statusObserver: StatusObserver)
    fun pause(trackId: Int)
    fun seek(trackId: Int, position: Float)
    fun release(trackId: Int)

    interface StatusObserver {
        fun onProgress(progress: Float)
        fun onStop()
        fun onPlay()
    }
}