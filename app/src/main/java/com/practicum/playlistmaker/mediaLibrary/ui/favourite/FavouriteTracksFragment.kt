package com.practicum.playlistmaker.mediaLibrary.ui.favourite

import android.os.Bundle
import android.util.Log
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
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private val viewModel: FavouriteTracksViewModel by viewModel()

    private lateinit var binding: FragmentFavouriteTracksBinding

    private var favouriteTrackAdapter: TrackAdapter? = null

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickDebounce()

        binding.rwFavouriteTracks.adapter = favouriteTrackAdapter

        viewModel.getFavouriteSharedFlow()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { favouriteState ->
                Log.d("My log", "favouriteState: $favouriteState")
                when (favouriteState) {
                    is FavouriteState.Content -> {
                        Log.d("My log", "tracks: ${favouriteState.tracks}")
                        showContent()
                    }
                    FavouriteState.Empty -> showEmpty()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        favouriteTrackAdapter = TrackAdapter {track->
            onTrackClickDebounce(track)
        }
    }

    private fun showContent() {
        with(binding) {
            favouriteTrackAdapter?.notifyDataSetChanged()
            rwFavouriteTracks.isVisible = true
            textViewEmpty.isVisible = false
            imageViewEmpty.isVisible = false
            newPlaylistButton.isVisible = false
        }
    }

    private fun showEmpty() {
        with(binding) {
            rwFavouriteTracks.isVisible = false
            textViewEmpty.isVisible = true
            imageViewEmpty.isVisible = true
            newPlaylistButton.isVisible = true
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
                R.id.action_searchFragment_to_mediaActivity,
                MediaActivity.createArgs(track)
            )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        fun newInstance() = FavouriteTracksFragment()
    }

}