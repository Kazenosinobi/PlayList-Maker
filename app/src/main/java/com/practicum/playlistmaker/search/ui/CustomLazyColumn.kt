package com.practicum.playlistmaker.search.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.debounce

@Composable
fun CustomLazyColumn(
    tracks: List<Track>,
    onClick: (Track) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val debouncedOnClick = remember {
        debounce(
            delayMillis = 100L,
            coroutineScope = coroutineScope,
            useLastParam = true,
            action = onClick
        )
    }
    LazyColumn {
        items(tracks) { track ->
            TrackItem(
                albumImage = track.coverArtworkMini,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTime = track.getTrackTime(),
            ) {
                debouncedOnClick(track)
            }
        }
    }
}
