package com.practicum.playlistmaker.search.ui

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
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter

class SearchActivity : ComponentActivity() {

    private var searchTextValue = SEARCH_TEXT_VALUE

    private var binding: ActivitySearchBinding? = null

    private var trackAdapter: TrackAdapter? = null
    private var trackHistoryAdapter: TrackAdapter? = null

    private val searchRunnable =
        Runnable { viewModel.search(binding?.editTextSearch?.toString()?.trim()) }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(application as App)
        )[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        trackAdapter = TrackAdapter { track ->
            viewModel.addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter = TrackAdapter { track ->
            viewModel.addToTrackHistory(track)
            startMediaActivity(track)
        }
        trackHistoryAdapter?.submitList(
            interactor.getSearchHistory().toMutableList()
        )
        binding?.searchBackButton?.setOnClickListener {
            finish()
        }

        binding?.imageViewSearchClear?.setOnClickListener {
            binding?.editTextSearch?.setText(SEARCH_TEXT_VALUE)
            hideKeyBoard()
            trackAdapter?.submitList(emptyList())
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.imageViewSearchClear?.isVisible = s.isNullOrEmpty().not()
                if (binding?.editTextSearch?.hasFocus() == true) {
                    showHistory()
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchTextValue = s?.toString() ?: ""
            }

        }

        binding?.editTextSearch?.addTextChangedListener(simpleTextWatcher)

        binding?.editTextSearch?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showHistory()
            }
        }

        binding?.editTextSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search(binding?.editTextSearch?.toString()?.trim())
            }
            false
        }

        binding?.reconnectButton?.setOnClickListener {
            viewModel.search(
                binding?.editTextSearch?.toString()?.trim()
            )
        }
        binding?.buttonClearHistory?.setOnClickListener { viewModel.clearHistory() }

        binding?.rwTrack?.adapter = trackAdapter
        binding?.rwSearchHistory?.adapter = trackHistoryAdapter
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
        binding?.editTextSearch?.setText(searchTextValue)
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

    private fun showHistory() {
        binding?.groupHistory?.isVisible = binding?.editTextSearch?.text.isNullOrEmpty()
                && trackHistoryAdapter?.currentList.isNullOrEmpty().not()
        binding.let {
            it?.rwTrack?.isVisible = false
            it?.llErrors?.isVisible = false
            it?.llNotInternet?.isVisible = false
        }
    }

    private fun startMediaActivity(track: Track) {
        if (clickDebounce()) {
            val mediaActivityIntent = MediaActivity.createMediaActivityIntent(this, track)
            startActivity(mediaActivityIntent)
        }
    }

    companion object {
        private const val SEARCH_TEXT_VALUE = ""
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L


        fun createSearchActivityIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}