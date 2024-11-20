package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null

    private var trackAdapter: TrackAdapter? = null
    private var trackHistoryAdapter: TrackAdapter? = null

    private var onTrackClickDebounce: ((Track) -> Unit?)? = null
    private var onSearchDebounce: ((String) -> Unit)? = null

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchDebounce()
        initClickDebounce()
        initAdapters()
        initListeners()
        observeFlow()
    }

    private fun observeFlow() {
        viewModel.getViewStateSharedFlow()
            .distinctUntilChanged()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { viewState ->
                when (viewState) {
                    ViewState.EmptyError -> showEmpty()
                    is ViewState.History -> showHistory(viewState.historyList)
                    ViewState.Loading -> showProgressBar()
                    ViewState.NetworkError -> showError()
                    is ViewState.Success -> showListTracks(viewState.trackList)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initListeners() {
        binding?.imageViewSearchClear?.setOnClickListener {
            binding?.editTextSearch?.setText(EMPTY_TEXT)
            hideKeyBoard()
            viewModel.needToShowHistory()
        }

        binding?.reconnectButton?.setOnClickListener {
            viewModel.search(binding?.editTextSearch?.text.toString())
        }
        binding?.buttonClearHistory?.setOnClickListener {
            viewModel.clearHistory()
            trackHistoryAdapter?.submitList(emptyList())
        }

        binding?.editTextSearch?.addTextChangedListener {
            binding?.imageViewSearchClear?.isVisible = it.isNullOrEmpty().not()
            if (binding?.editTextSearch?.hasFocus() == true) {
                viewModel.needToShowHistory()
            }
            onSearchDebounce?.let { it1 -> it1(it.toString()) }
        }

        binding?.editTextSearch?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.needToShowHistory()
            }
        }
    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter { track ->
            viewModel.addToTrackHistory(track)
            onTrackClickDebounce?.let { it(track) }
        }
        trackHistoryAdapter = TrackAdapter { track ->
            viewModel.addToTrackHistory(track)
            onTrackClickDebounce?.let { it(track) }
            viewModel.needToShowHistory()
        }

        binding?.rwTrack?.adapter = trackAdapter
        binding?.rwTrack?.itemAnimator = null
        binding?.rwSearchHistory?.adapter = trackHistoryAdapter
    }

    private fun initSearchDebounce() {
        onSearchDebounce = debounce(
            SEARCH_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { query ->
            viewModel.search(query)
        }
    }

    private fun initClickDebounce() {
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            startMediaActivity(track)
        }
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
        trackAdapter?.submitList(listTracks)
        binding?.let {
            it.rwTrack.isVisible = true
            it.llErrors.isVisible = false
            it.llNotInternet.isVisible = false
            it.progressBar.isVisible = false
        }
    }

    private fun showProgressBar() {
        binding?.let {
            it.rwTrack.isVisible = false
            it.llErrors.isVisible = false
            it.llNotInternet.isVisible = false
            it.groupHistory.isVisible = false
            it.progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        binding?.let {
            it.rwTrack.isVisible = false
            it.llErrors.isVisible = true
            it.llNotInternet.isVisible = false
            it.groupHistory.isVisible = false
            it.progressBar.isVisible = false
        }
    }

    private fun showError() {
        binding?.let {
            it.rwTrack.isVisible = false
            it.llNotInternet.isVisible = true
            it.llErrors.isVisible = false
            it.groupHistory.isVisible = false
            it.progressBar.isVisible = false
        }
    }

    private fun showHistory(historyList: List<Track>) {
        trackAdapter?.submitList(emptyList())
        trackHistoryAdapter?.submitList(historyList)
        binding?.groupHistory?.isVisible = binding?.editTextSearch?.text.isNullOrEmpty()
                && historyList.isEmpty().not()
        binding?.let {
            it.rwTrack.isVisible = false
            it.llErrors.isVisible = false
            it.llNotInternet.isVisible = false
            it.progressBar.isVisible = false
        }
    }

    private fun startMediaActivity(track: Track) {
        findNavController()
            .navigate(
                R.id.action_searchFragment_to_mediaActivity,
                MediaActivity.createArgs(track)
            )
    }

    private companion object {
        private const val EMPTY_TEXT = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }
}