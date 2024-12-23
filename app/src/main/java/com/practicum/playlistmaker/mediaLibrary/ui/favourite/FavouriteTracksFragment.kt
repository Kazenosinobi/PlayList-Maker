package com.practicum.playlistmaker.mediaLibrary.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.practicum.playlistmaker.media.ui.MediaFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private val viewModel: FavouriteTracksViewModel by viewModel()

    private var binding: FragmentFavouriteTracksBinding? = null

    private var favouriteTrackAdapter: TrackAdapter? = null

    private var onTrackClickDebounce: ((Track) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickDebounce()
        initAdapters()
        observeFlow()
    }

    private fun initAdapters() {
        favouriteTrackAdapter = TrackAdapter { track ->
            onTrackClickDebounce?.let { it(track) }
        }

        binding?.rwFavouriteTracks?.adapter = favouriteTrackAdapter
    }

    private fun observeFlow() {
        viewModel.getFavouriteSharedFlow()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { favouriteState ->
                when (favouriteState) {
                    is FavouriteState.Content -> {
                        showContent(favouriteState.tracks)
                    }

                    FavouriteState.Empty -> {
                        showEmpty()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showContent(tracks: List<Track>) {
        binding?.let {
            favouriteTrackAdapter?.submitList(tracks)
            it.rwFavouriteTracks.isVisible = true
            it.textViewEmpty.isVisible = false
            it.imageViewEmpty.isVisible = false
        }
    }

    private fun showEmpty() {
        binding?.let {
            it.rwFavouriteTracks.isVisible = false
            it.textViewEmpty.isVisible = true
            it.imageViewEmpty.isVisible = true
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

    private fun startMediaActivity(track: Track) {
        findNavController()
            .navigate(
                R.id.action_mediaLibraryFragment_to_mediaFragment,
                MediaFragment.createArgs(track)
            )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        fun newInstance() = FavouriteTracksFragment()
    }

}