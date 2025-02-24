package com.practicum.playlistmaker.search.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier,
    search: (String) -> Unit,
    showHistory: () -> Unit,
) {
    var query = rememberSaveable { mutableStateOf("") }
    var searchJob: Job? by remember { mutableStateOf(null) }
    LaunchedEffect(query) {
        searchJob?.cancel()
        searchJob = launch {
            delay(2000)
            search(query.value)
        }
        if (query.value.isEmpty()) {
            showHistory()
        }
    }
    SearchBar(
        modifier = modifier,
        inputField = {
            TextField(
                value = query.value,
                onValueChange = { query.value = it},
                placeholder = {
                    Text(
                        text = stringResource(R.string.text_search),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = stringResource(R.string.clear_button),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                trailingIcon = {
                    val focusManager = LocalFocusManager.current
                    if (query.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query.value = ""
                                showHistory()
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.clear),
                                contentDescription = stringResource(R.string.clear_button),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    errorContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.surface,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { search(query.value) }
                )
            )
        },
        expanded = false,
        onExpandedChange = {  },
        content = { },
    )
}