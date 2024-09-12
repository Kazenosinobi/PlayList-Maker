package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val executor: ExecutorService
) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }

    override fun getSearchHistory(): Array<Track> {
        return repository.getSearchHistory()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        repository.saveSearchTrackHistory(tracks)
    }

    override fun addToTrackHistory(track: Track) {
        repository.addToTrackHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}