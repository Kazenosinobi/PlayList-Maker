package com.practicum.playlistmaker.playListScreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.ui.PlayListMenuBottomSheetFragment
import com.practicum.playlistmaker.databinding.FragmentPlayListScreenBinding
import com.practicum.playlistmaker.media.ui.MediaFragment
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
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
            startSharing()
        }

        binding?.imageViewOptions?.setOnClickListener {
            viewModel.playList?.let { playList -> startPlayListMenuBottomSheetFragment(playList) }
        }
    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter(
            onClick = { track ->
            onPlayListClickDebounce?.let { it(track) }
            },
            onLongClickListener = { track ->
                showDeleteTrackDialog(track)
            }
        )
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

    private fun startPlayListMenuBottomSheetFragment(playList: PlayListData) {
        findNavController()
            .navigate(
                R.id.action_playListScreenFragment_to_playListMenuBottomSheetFragment2,
                PlayListMenuBottomSheetFragment.createArgs(playList)
            )
    }

    private fun observeFlow() {
        viewModel.getPlayListScreenStateFlow()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { playListScreenState ->
                when (playListScreenState) {
                    is PlayListScreenState.Content -> showContent(
                        playListScreenState.playList, playListScreenState.tracks
                    )
                    PlayListScreenState.Empty -> Unit
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showContent(playList: PlayListData, tracks: List<Track>) {
        trackAdapter?.submitList(tracks)
        setText(playList, tracks)
        getImageAlbum(playList)
        if (tracks.isEmpty()) {
            binding?.rwTracksList?.isVisible = false
            binding?.imageViewEmpty?.isVisible = true
            binding?.textViewEmpty?.isVisible = true
        } else {
            binding?.rwTracksList?.isVisible = true
            binding?.imageViewEmpty?.isVisible = false
            binding?.textViewEmpty?.isVisible = false
        }
    }

    private fun getImageAlbum(playList: PlayListData) {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        binding?.let {
            Glide.with(requireContext())
                .load(playList.image)
                .placeholder(R.drawable.place_holder)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(cornerRadius)))
                .into(it.imageViewAlbum)
        }
    }

    private fun setText(playList: PlayListData, tracks: List<Track>) {
        binding?.let {
            with(it) {
                textViewAlbumName.text = playList.nameOfAlbum
                textViewDescription.text = getDescription(playList)
                textViewTotalDuration.text = getTotalDurationText(playList, tracks)
                textViewTotalTracks.text = getTotalTracksText(tracks)
            }
        }
    }

    private fun getDescription(playList: PlayListData): String {
        val description = playList.descriptionOfAlbum
            binding.let {
                it?.textViewDescription?.isVisible = description.isNullOrBlank().not()
            }
        return description.orEmpty()
    }

    private fun getTotalDurationText(playList: PlayListData, tracks: List<Track>): String {

        val totalDuration = playList.getTotalDuration(tracks).toInt()
        return resources.getQuantityString(
            R.plurals.minutes_count,
            totalDuration,
            totalDuration
            )
        }

    private fun getTotalTracksText(tracks: List<Track>): String {
        val tracksCount = tracks.size
        return resources.getQuantityString(
            R.plurals.tracks_count,
            tracksCount,
            tracksCount
            )
    }

    private fun setUpBottomSheetHeight() {
        val bottomSheetDimensions = BottomSheetDimensions(requireActivity())
        binding?.llBottomSheet?.let {container ->
            bottomSheetDimensions.setupBottomSheetHeightForDialog(
                container,
                PERCENT_OF_BOTTOM_SHEET_HEIGHT
            )
        }
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.want_to_delete_a_track)
            .setNeutralButton(R.string.no) { _, _ ->

            }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.removeTrackFromPlayList(track)
            }
            .show()
    }

    private fun startSharing() {
        if (trackAdapter?.currentList?.size == 0) {
            Toast.makeText(
                requireContext(),
                R.string.no_tracks,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.sharePlayList()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        private const val PERCENT_OF_BOTTOM_SHEET_HEIGHT = 0.37f
        private const val EXTRA_PLAY_LIST_ID = "extra_play_list_id"

        fun createArgs(playListId: Int): Bundle {
            val jsonString = Json.encodeToString(playListId)
            return bundleOf(EXTRA_PLAY_LIST_ID to jsonString)
        }
    }
}