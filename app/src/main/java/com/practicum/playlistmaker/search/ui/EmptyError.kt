package com.practicum.playlistmaker.search.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.theme.FirstComposeProjectYATheme

@Composable
fun EmptyError() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 102.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.light_mode_nothing),
            contentDescription = stringResource(R.string.nothing_was_found)
        )
        Spacer(
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.nothing_was_found),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
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
private fun EmptyErrorPreview() {
    FirstComposeProjectYATheme {
        EmptyError()
    }
}