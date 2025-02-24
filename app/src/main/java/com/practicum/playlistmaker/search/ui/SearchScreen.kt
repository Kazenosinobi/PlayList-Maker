package com.practicum.playlistmaker.search.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.ui.MediaFragment
import com.practicum.playlistmaker.search.domain.models.ViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(navController: NavController) {

    val viewModel: SearchViewModel = koinViewModel()
    val viewState = viewModel.getViewStateFlow().collectAsStateWithLifecycle()
    Scaffold(topBar = { CustomTopAppBar() }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            CustomSearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                search = { query -> viewModel.search(query) },
                showHistory = { viewModel.needToShowHistory() })
            Spacer(modifier = Modifier.padding(16.dp))
            AnimatedContent(
                targetState = viewState, label = ""
            ) { viewState ->
                when (viewState.value) {
                    is ViewState.History -> {
                        HistoryLazyColumn(
                            tracks = (viewState.value as ViewState.History).historyList,
                            onClick = { track ->
                                navController.navigate(
                                    R.id.action_searchFragment_to_mediaFragment,
                                    MediaFragment.createArgs(track)
                                )
                                viewModel.addToTrackHistory(track)
                            },
                            onButtonClick = {
                                viewModel.clearHistory()
                            })
                    }

                    is ViewState.Success -> {
                        CustomLazyColumn(
                            tracks = (viewState.value as ViewState.Success).trackList,
                            onClick = { track ->
                                navController.navigate(
                                    R.id.action_searchFragment_to_mediaFragment,
                                    MediaFragment.createArgs(track)
                                )
                                viewModel.addToTrackHistory(track)
                            })
                    }

                    ViewState.EmptyError -> EmptyError()
                    ViewState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.surface)
                        }
                    }

                    ViewState.NetworkError -> NetworkError(onButtonClick = { viewModel.triggerLastSearch() })
                    ViewState.Initial -> Unit
                }
            }
        }
    }
}