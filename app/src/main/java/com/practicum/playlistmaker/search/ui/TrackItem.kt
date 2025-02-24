package com.practicum.playlistmaker.search.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.theme.FirstComposeProjectYATheme

@Composable
fun TrackItem(
    albumImage: String?,
    trackName: String,
    artistName: String,
    trackTime: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 12.dp)
            .background(Color.Transparent)
            .clickable(
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(albumImage)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.album_image),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(2.dp))
        )
        Spacer(
            modifier = Modifier.padding(start = 8.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = trackName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .wrapContentWidth(),
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f, false),
                    text = artistName,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(
                    modifier = Modifier.padding(start = 4.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ellipse_1),
                    contentDescription = stringResource(R.string.dot),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = trackTime,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(R.drawable.terms_of_use),
            contentDescription = stringResource(R.string.play),
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TrackItemPreview() {
    FirstComposeProjectYATheme {
        TrackItem(
            albumImage = null,
            trackName = "Track1Track1Track1Track1Track1Track1Track1Track1Track1Track1",
            artistName = "Track1Track1Track1Track1Track1Track1Track1Track1Track1",
            trackTime = "Track1",
            onClick = {}
        )
    }
}