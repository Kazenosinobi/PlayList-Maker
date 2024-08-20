package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.core.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.recycler.TrackAdapter
import com.practicum.playlistmaker.media.presentation.MediaActivity

class SearchActivity : AppCompatActivity() {

    private var searchTextValue = SEARCH_TEXT_VALUE

    private var editTextSearch: EditText? = null

    private var progressBar: ProgressBar? = null

    private var imageViewSearchClear: ImageView? = null
    private var searchBackButton: ImageView? = null

    private var llErrors: LinearLayout? = null
    private var llNotInternet: LinearLayout? = null
    private var groupHistory: Group? = null

    private var rwTrack: RecyclerView? = null
    private var rwSearchHistory: RecyclerView? = null
    private var reconnectButton: Button? = null
    private var buttonClearHistory: Button? = null

    private var trackAdapter: TrackAdapter? = null
    private var trackHistoryAdapter: TrackAdapter? = null

    private val searchRunnable = Runnable { search() }

    private val interactor by lazy { Creator.provideTracksInteractor(this) }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()

        trackAdapter = TrackAdapter { track ->
            addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter = TrackAdapter { track ->
            addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter?.tracks?.addAll(
            interactor.getSearchHistory() ?: emptyArray()
        )
        searchBackButton?.setOnClickListener {
            finish()
        }

        imageViewSearchClear?.setOnClickListener {
            editTextSearch?.setText("")
            hideKeyBoard()
            trackAdapter?.tracks?.clear()
            showHistory()
            trackAdapter?.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageViewSearchClear?.isVisible = s.isNullOrEmpty().not()
                if (editTextSearch?.hasFocus() == true) {
                    showHistory()
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchTextValue = s?.toString() ?: ""
            }

        }

        editTextSearch?.addTextChangedListener(simpleTextWatcher)

        editTextSearch?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showHistory()
            }
        }

        editTextSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        reconnectButton?.setOnClickListener { search() }
        buttonClearHistory?.setOnClickListener { clearHistory() }

        rwTrack?.adapter = trackAdapter
        rwSearchHistory?.adapter = trackHistoryAdapter
    }

    override fun onStop() {
        super.onStop()
        interactor.saveSearchTrackHistory(
            trackHistoryAdapter?.tracks?.toTypedArray() ?: emptyArray()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_VALUE)
        editTextSearch?.setText(searchTextValue)
    }

    private fun initViews() {
        searchBackButton = findViewById(R.id.searchBackButton)
        editTextSearch = findViewById(R.id.editTextSearch)
        imageViewSearchClear = findViewById(R.id.imageViewSearchClear)
        rwTrack = findViewById(R.id.rwTrack)
        llErrors = findViewById(R.id.llErrors)
        llNotInternet = findViewById(R.id.llNotInternet)
        reconnectButton = findViewById(R.id.reconnectButton)
        groupHistory = findViewById(R.id.groupHistory)
        rwSearchHistory = findViewById(R.id.rwSearchHistory)
        buttonClearHistory = findViewById(R.id.buttonClearHistory)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun showListTracks(listTracks: List<Track>) {
        trackAdapter?.tracks?.clear()
        trackAdapter?.tracks?.addAll(listTracks)
        trackAdapter?.notifyDataSetChanged()
        rwTrack?.isVisible = true
        llErrors?.isVisible = false
        llNotInternet?.isVisible = false
        progressBar?.isVisible = false
    }

    private fun search() {
        if (editTextSearch?.text.isNullOrEmpty()) return
        showProgressBar()

        interactor.searchTracks(editTextSearch?.text.toString().trim()) { listTracks ->
            if (listTracks.isNotEmpty()) {
                handler.post { showListTracks(listTracks) }
            } else {
                handler.post { showError() }
            }
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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
        trackHistoryAdapter?.tracks?.clear()
        interactor.saveSearchTrackHistory(
            trackHistoryAdapter?.tracks?.toTypedArray() ?: emptyArray()
        )
        trackHistoryAdapter?.notifyDataSetChanged()
        groupHistory?.isVisible = false
    }

    private fun showProgressBar() {
        rwTrack?.isVisible = false
        llErrors?.isVisible = false
        llNotInternet?.isVisible = false
        groupHistory?.isVisible = false
        progressBar?.isVisible = true
    }

    private fun showEmpty() {
        rwTrack?.isVisible = false
        llErrors?.isVisible = true
        llNotInternet?.isVisible = false
        groupHistory?.isVisible = false
        progressBar?.isVisible = false
    }

    private fun showError() {
        rwTrack?.isVisible = false
        llNotInternet?.isVisible = true
        llErrors?.isVisible = false
        groupHistory?.isVisible = false
        progressBar?.isVisible = false
    }

    private fun showHistory() {
        groupHistory?.isVisible =
            editTextSearch?.text.isNullOrEmpty()
                    && trackHistoryAdapter?.tracks.isNullOrEmpty().not()
        rwTrack?.isVisible = false
        llErrors?.isVisible = false
        llNotInternet?.isVisible = false
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun addToTrackHistory(track: Track) {
        val tracks = trackHistoryAdapter?.tracks ?: return
        val existingTrackIndex = tracks.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            tracks.removeAt(existingTrackIndex)
            trackHistoryAdapter?.notifyItemRemoved(existingTrackIndex)
        }
        if (tracks.size >= TRACKS_HISTORY_MAX_SIZE) {
            tracks.removeAt(tracks.lastIndex)
            trackHistoryAdapter?.notifyItemRemoved(tracks.lastIndex)
        }
        tracks.add(0, track)
        trackHistoryAdapter?.notifyItemInserted(0)
        trackHistoryAdapter?.notifyItemRangeChanged(0, tracks.size)
        interactor.saveSearchTrackHistory(
            trackHistoryAdapter?.tracks?.toTypedArray() ?: emptyArray()
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