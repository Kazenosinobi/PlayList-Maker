package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private var binding: ActivitySearchBinding? = null

    private var trackAdapter: TrackAdapter? = null
    private var trackHistoryAdapter: TrackAdapter? = null

    private val searchRunnable =
        Runnable { viewModel.search(binding?.editTextSearch?.text.toString()) }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getViewModelFactory(application as App)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        viewModel.getCurrentPositionLiveData().observe(this) { viewState ->
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

        binding?.searchBackButton?.setOnClickListener {
            finish()
        }

        binding?.imageViewSearchClear?.setOnClickListener {
            binding?.editTextSearch?.setText(EMPTY_TEXT)
            hideKeyBoard()
            viewModel.needToShowHistory()
        }

        binding?.editTextSearch?.doOnTextChanged { text, _, _, _ ->
            binding?.imageViewSearchClear?.isVisible = text.isNullOrEmpty().not()
            if (binding?.editTextSearch?.hasFocus() == true) {
                viewModel.needToShowHistory()
            }
            searchDebounce()
        }

        binding?.editTextSearch?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.needToShowHistory()
            }
        }

        binding?.reconnectButton?.setOnClickListener {
            viewModel.search(binding?.editTextSearch?.text.toString())
        }
        binding?.buttonClearHistory?.setOnClickListener {
            viewModel.clearHistory()
            trackHistoryAdapter?.submitList(emptyList())
        }

        binding?.rwTrack?.adapter = trackAdapter
        binding?.rwSearchHistory?.adapter = trackHistoryAdapter
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
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
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun showListTracks(listTracks: List<Track>) {
        trackAdapter?.submitList(emptyList())
        trackAdapter?.submitList(listTracks)
        binding.let {
            it?.rwTrack?.isVisible = true
            it?.llErrors?.isVisible = false
            it?.llNotInternet?.isVisible = false
            it?.progressBar?.isVisible = false
        }
    }

    private fun showProgressBar() {
        binding.let {
            it?.rwTrack?.isVisible = false
            it?.llErrors?.isVisible = false
            it?.llNotInternet?.isVisible = false
            it?.groupHistory?.isVisible = false
            it?.progressBar?.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.let {
            it?.rwTrack?.isVisible = false
            it?.llErrors?.isVisible = true
            it?.llNotInternet?.isVisible = false
            it?.groupHistory?.isVisible = false
            it?.progressBar?.isVisible = false
        }
    }

    private fun showError() {
        binding.let {
            it?.rwTrack?.isVisible = false
            it?.llNotInternet?.isVisible = true
            it?.llErrors?.isVisible = false
            it?.groupHistory?.isVisible = false
            it?.progressBar?.isVisible = false
        }
    }

    private fun showHistory(historyList: List<Track>) {
        trackHistoryAdapter?.submitList(historyList)
        binding?.groupHistory?.isVisible = binding?.editTextSearch?.text.isNullOrEmpty()
                && historyList.isEmpty().not()
        binding.let {
            it?.rwTrack?.isVisible = false
            it?.llErrors?.isVisible = false
            it?.llNotInternet?.isVisible = false
            it?.progressBar?.isVisible = false
        }
    }

    private fun startMediaActivity(track: Track) {
        if (clickDebounce()) {
            val mediaActivityIntent = MediaActivity.createMediaActivityIntent(this, track)
            startActivity(mediaActivityIntent)
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun createSearchActivityIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}