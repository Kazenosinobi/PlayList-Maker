package com.practicum.playlistmaker.playListScreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListScreenBinding
import com.practicum.playlistmaker.media.ui.MediaFragment
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.recycler.TrackAdapter
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListScreenFragment : Fragment() {

    private var binding: FragmentPlayListScreenBinding? = null

    private var trackAdapter: TrackAdapter? = null

    private var onPlayListClickDebounce: ((Track) -> Unit)? = null

    private val viewModel: PlayListScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickDebounce()
        initAdapters()
        initListeners()
        observeFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackAdapter = null
    }

    private fun initListeners() {
        binding?.backButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        binding?.imageViewShare?.setOnClickListener {

        }

        binding?.imageViewOptions?.setOnClickListener {

        }
    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter { track ->
            onPlayListClickDebounce?.let { it(track) }
        }
        binding?.rwTracksList?.adapter = trackAdapter
    }

    private fun initClickDebounce() {
        onPlayListClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            startMediaFragment(track)
        }
    }

    private fun startMediaFragment(track: Track) {
        findNavController()
            .navigate(
                R.id.action_playListScreenFragment_to_mediaFragment,
                MediaFragment.createArgs(track)
            )
    }

    private fun observeFlow() {
        viewModel.getPlayListScreenStateFlow()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { playListScreenState ->
                when (playListScreenState) {
                    is PlayListScreenState.Content -> showContent(playListScreenState.playList)
                    PlayListScreenState.Empty -> Unit
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showContent(playList: PlayListCreateData) {
        trackAdapter?.submitList(playList.tracks)
        setText(playList)
        getImageAlbum(playList)
    }

    private fun getImageAlbum(playList: PlayListCreateData) {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        binding?.let {
            Glide.with(this)
                .load(playList.image)
                .placeholder(R.drawable.place_holder)
                .transform(RoundedCorners(cornerRadius))
                .into(it.imageViewAlbum)
        }
    }

    private fun setText(playList: PlayListCreateData) {
        binding?.let {
            with(it) {
                textViewAlbumName.text = playList.nameOfAlbum
                textViewDescription.text = getDescription(playList)
                textViewTotalDuration.text = playList.getTotalDuration(playList.tracks)
                textViewTotalTracks.text = playList.tracks.size.toString()
            }
        }
    }

    private fun getDescription(playList: PlayListCreateData): String {
        return playList.descriptionOfAlbum ?: run {
            binding.let {
                it?.textViewDescription?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        private const val EMPTY_STRING = ""
    }
}