package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private var trackAdapter: TrackAdapter? = null
    private var trackHistoryAdapter: TrackAdapter? = null

    private val searchRunnable =
        Runnable { viewModel.search(binding.editTextSearch.text.toString()) }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentPositionLiveData().observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                ViewState.EmptyError -> showEmpty()
                is ViewState.History -> showHistory(viewState.historyList)
                ViewState.Loading -> showProgressBar()
                ViewState.NetworkError -> showError()
                is ViewState.Success -> showListTracks(viewState.trackList)
            }
        }

        trackAdapter = TrackAdapter { track ->
            viewModel.addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter = TrackAdapter { track ->
            viewModel.addToTrackHistory(track)
            startMediaActivity(track)
            viewModel.needToShowHistory()
        }

        binding.imageViewSearchClear.setOnClickListener {
            binding.editTextSearch.setText(EMPTY_TEXT)
            hideKeyBoard()
            viewModel.needToShowHistory()
        }

        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            binding.imageViewSearchClear.isVisible = text.isNullOrEmpty().not()
            if (binding.editTextSearch.hasFocus()) {
                viewModel.needToShowHistory()
            }
            searchDebounce()
        }

        binding.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.needToShowHistory()
            }
        }

        binding.reconnectButton.setOnClickListener {
            viewModel.search(binding.editTextSearch.text.toString())
        }
        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            trackHistoryAdapter?.submitList(emptyList())
        }

        binding.rwTrack.adapter = trackAdapter
        binding.rwSearchHistory.adapter = trackHistoryAdapter
    }

    private fun searchDebounce() {
        with(handler) {
            removeCallbacks(searchRunnable)
            postDelayed(
                searchRunnable,
                SEARCH_DEBOUNCE_DELAY
            )
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = activity?.currentFocus ?: view
        view?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun showListTracks(listTracks: List<Track>) {
        trackAdapter?.submitList(emptyList())
        trackAdapter?.submitList(listTracks)
        with(binding) {
            rwTrack.isVisible = true
            llErrors.isVisible = false
            llNotInternet.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showProgressBar() {
        with(binding) {
            rwTrack.isVisible = false
            llErrors.isVisible = false
            llNotInternet.isVisible = false
            groupHistory.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        with(binding) {
            rwTrack.isVisible = false
            llErrors.isVisible = true
            llNotInternet.isVisible = false
            groupHistory.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        with(binding) {
            rwTrack.isVisible = false
            llNotInternet.isVisible = true
            llErrors.isVisible = false
            groupHistory.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showHistory(historyList: List<Track>) {
        trackHistoryAdapter?.submitList(historyList)
        binding.groupHistory.isVisible = binding.editTextSearch.text.isNullOrEmpty()
                && historyList.isEmpty().not()
        with(binding) {
            rwTrack.isVisible = false
            llErrors.isVisible = false
            llNotInternet.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun startMediaActivity(track: Track) {
        if (clickDebounce()) {
            findNavController()
                .navigate(
                    R.id.action_searchFragment_to_mediaActivity,
                    MediaActivity.createArgs(track)
                )
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}