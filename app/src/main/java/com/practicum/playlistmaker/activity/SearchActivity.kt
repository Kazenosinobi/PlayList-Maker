package com.practicum.playlistmaker.activity

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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.api.iTunesApi
import com.practicum.playlistmaker.application.App.Companion.PREFERENCES
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.models.Track
import com.practicum.playlistmaker.models.TracksResponse
import com.practicum.playlistmaker.recycler.TrackAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private var searchHistory: SearchHistory? = null

    private val searchRunnable = Runnable { search() }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val iTunesService = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(iTunesApi::class.java)

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
        searchHistory = SearchHistory(getSharedPreferences(PREFERENCES, MODE_PRIVATE))
        trackHistoryAdapter?.tracks?.addAll(
            searchHistory?.getSearchHistory() ?: emptyArray()
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
        searchHistory?.saveSearchTrackHistory(
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

    private fun search() {
        if (editTextSearch?.text.isNullOrEmpty()) return
        showProgressBar()
        iTunesService.searchTracks(editTextSearch?.text.toString().trim())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>,
                ) {
                    val result = response.body()?.results
                    if (result != null) {
                        when (response.code()) {
                            200 -> {
                                if (result.isNotEmpty()) {
                                    trackAdapter?.tracks?.clear()
                                    trackAdapter?.tracks?.addAll(result)
                                    trackAdapter?.notifyDataSetChanged()
                                    rwTrack?.isVisible = true
                                    llErrors?.isVisible = false
                                    llNotInternet?.isVisible = false
                                    progressBar?.isVisible = false
                                } else {
                                    showEmpty()
                                }
                            }

                            else -> {
                                showError()
                            }
                        }
                    } else {
                        showEmpty()
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showError()
                }

            })
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
        searchHistory?.saveSearchTrackHistory(
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
        searchHistory?.saveSearchTrackHistory(
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