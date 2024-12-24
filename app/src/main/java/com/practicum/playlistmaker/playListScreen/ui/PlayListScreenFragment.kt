package com.practicum.playlistmaker.playListScreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayListScreenFragment : Fragment() {

    private val playListId by lazy {
        val jsonString = requireArguments().getString(EXTRA_PLAY_LIST_ID) ?: ""
        Json.decodeFromString<Int>(jsonString)
    }

    private var binding: FragmentPlayListScreenBinding? = null

    private var trackAdapter: TrackAdapter? = null

    private var onPlayListClickDebounce: ((Track) -> Unit)? = null

    private val viewModel: PlayListScreenViewModel by viewModel {
        parametersOf(playListId)
    }

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
        setUpBottomSheetHeight()
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
                textViewTotalDuration.text = getTotalDurationText(playList)
                textViewTotalTracks.text = getTotalTracksText(playList)
            }
        }
    }

    private fun getDescription(playList: PlayListCreateData): String {
        return playList.descriptionOfAlbum.takeIf {
            it.isNullOrBlank().not()
        } ?: run {
            binding.let {
                it?.textViewDescription?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getTotalDurationText(playList: PlayListCreateData): String? {
        val resources = binding?.root?.context?.resources
        val totalDuration = playList.getTotalDuration(playList.tracks).toInt()
        return resources?.getQuantityString(
            R.plurals.minutes_count,
            totalDuration,
            totalDuration
        )
    }

    private fun getTotalTracksText(playList: PlayListCreateData): String? {
        val resources = binding?.root?.context?.resources
        val tracksCount = playList.tracks.size
        return resources?.getQuantityString(
            R.plurals.tracks_count,
            tracksCount,
            tracksCount
        )
    }

    private fun setUpBottomSheetHeight() {
        val bottomSheetDimensions = BottomSheetDimensions(requireActivity())
        binding?.llBottomSheet?.let {container ->
            bottomSheetDimensions.setupBottomSheetHeight(
                container,
                PERCENT_OF_BOTTOM_SHEET_HEIGHT
            )
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        private const val PERCENT_OF_BOTTOM_SHEET_HEIGHT = 0.37f
        private const val EMPTY_STRING = ""
        private const val EXTRA_PLAY_LIST_ID = "extra_play_list_id"

        fun createArgs(playListId: Int): Bundle {
            val jsonString = Json.encodeToString(playListId)
            return bundleOf(EXTRA_PLAY_LIST_ID to jsonString)
        }
    }
}