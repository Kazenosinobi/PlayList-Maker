package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlistmaker.core.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.media.presentation.MediaActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import com.practicum.playlistmaker.search.presentation.recycler.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private var searchTextValue = SEARCH_TEXT_VALUE

    private lateinit var binding: ActivitySearchBinding

    private var trackAdapter: TrackAdapter? = null
    private var trackHistoryAdapter: TrackAdapter? = null

    private val searchRunnable = Runnable { search() }

    private val interactor by lazy { Creator.provideTracksInteractor(this) }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        trackAdapter = TrackAdapter { track ->
            addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter = TrackAdapter { track ->
            addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter?.submitList(
            interactor.getSearchHistory().toMutableList()
        )
        binding.searchBackButton.setOnClickListener {
            finish()
        }

        binding.imageViewSearchClear.setOnClickListener {
            binding.editTextSearch.setText(SEARCH_TEXT_VALUE)
            hideKeyBoard()
            trackAdapter?.submitList(emptyList())
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imageViewSearchClear.isVisible = s.isNullOrEmpty().not()
                if (binding.editTextSearch.hasFocus()) {
                    showHistory()
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchTextValue = s?.toString() ?: ""
            }

        }

        binding.editTextSearch.addTextChangedListener(simpleTextWatcher)

        binding.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showHistory()
            }
        }

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        binding.reconnectButton.setOnClickListener { search() }
        binding.buttonClearHistory.setOnClickListener { clearHistory() }

        binding.rwTrack.adapter = trackAdapter
        binding.rwSearchHistory.adapter = trackHistoryAdapter
    }

    override fun onStop() {
        super.onStop()
        interactor.saveSearchTrackHistory(
            trackHistoryAdapter?.currentList?.toTypedArray() ?: emptyArray()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_VALUE)
        binding.editTextSearch.setText(searchTextValue)
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

    private fun search() {
        if (binding.editTextSearch.text.isNullOrEmpty()) return
        showProgressBar()

        interactor.searchTracks(
            binding.editTextSearch.text.toString().trim()
        ) { viewState ->
            handler.post {
                when(viewState) {
                    is ViewState.Success -> showListTracks(viewState.trackList)
                    is ViewState.EmptyError -> showEmpty()
                    is ViewState.NetworkError -> showError()
                }
            }
        }
    }

    private fun searchDebounce() {
        with(handler) {
            removeCallbacks(searchRunnable)
            postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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

    private fun clearHistory() {
        trackHistoryAdapter?.submitList(emptyList())
        interactor.saveSearchTrackHistory(emptyArray())
        binding.groupHistory.isVisible = false
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

    private fun showHistory() {
        binding.groupHistory.isVisible =
            binding.editTextSearch.text.isNullOrEmpty()
                    && trackHistoryAdapter?.currentList.isNullOrEmpty().not()
        with(binding) {
            rwTrack.isVisible = false
            llErrors.isVisible = false
            llNotInternet.isVisible = false
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun addToTrackHistory(track: Track) {
        val currentTracks = trackHistoryAdapter?.currentList?.toMutableList() ?: mutableListOf()
        val existingTrackIndex = currentTracks.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            currentTracks.removeAt(existingTrackIndex)
        }
        if (currentTracks.size >= TRACKS_HISTORY_MAX_SIZE) {
            currentTracks.removeAt(currentTracks.lastIndex)
        }
        currentTracks.add(0, track)
        trackHistoryAdapter?.submitList(currentTracks)
        interactor.saveSearchTrackHistory(
            currentTracks.toTypedArray()
        )
    }

    private fun startMediaActivity(track: Track) {
        if (clickDebounce()) {
            val mediaActivityIntent = MediaActivity.createMediaActivityIntent(this, track)
            startActivity(mediaActivityIntent)
        }
    }

    companion object {
        const val SEARCH_TEXT_VALUE = ""
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val TRACKS_HISTORY_MAX_SIZE = 10
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L

        fun createSearchActivityIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}