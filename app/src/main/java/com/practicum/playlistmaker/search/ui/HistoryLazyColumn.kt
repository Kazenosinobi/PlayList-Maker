package com.practicum.playlistmaker.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.debounce

@Composable
fun HistoryLazyColumn(
    tracks: List<Track>,
    onClick: (Track) -> Unit,
    onButtonClick: () -> Unit,
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
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Text(
            text = stringResource(R.string.u_searched),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))
        LazyColumn(
            modifier = Modifier.weight(1f, false)
        ) {
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
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Button(
            onClick = { onButtonClick() },
            content = {
                Text(
                    text = stringResource(R.string.clear_history),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.displaySmall,
                )
            },
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}
