package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.data.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

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
}