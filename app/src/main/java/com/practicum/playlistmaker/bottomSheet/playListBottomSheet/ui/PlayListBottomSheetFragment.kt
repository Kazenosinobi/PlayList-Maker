package com.practicum.playlistmaker.bottomSheet.playListBottomSheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.databinding.FragmentPlayListBottomSheetBinding
import com.practicum.playlistmaker.mediaLibrary.ui.playList.PlayListState
import com.practicum.playlistmaker.bottomSheet.playListBottomSheet.ui.recycler.PlayListBottomSheetAdapter
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListBottomSheetFragment : BottomSheetDialogFragment() {

    private val track by lazy {
        val jsonString = requireArguments().getString(EXTRA_TRACK) ?: ""
        Json.decodeFromString<Track>(jsonString)
    }

    private val viewModel: PlayListBottomSheetViewModel by viewModel()

    private var binding: FragmentPlayListBottomSheetBinding? = null

    private var playListBottomSheetAdapter: PlayListBottomSheetAdapter? = null

    private var onPlayListClickDebounce: ((PlayListCreateData) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rwPlayLists?.itemAnimator = null

        initListeners()
        initAdapters()
        initClickDebounce()
        observeFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playListBottomSheetAdapter = null
    }

    private fun initListeners() {
        binding?.buttonNewPlayList?.setOnClickListener {
            startPlayListCreateFragment()
        }
    }

    private fun initAdapters() {
        playListBottomSheetAdapter = PlayListBottomSheetAdapter { playList ->
            onPlayListClickDebounce?.let { it(playList) }
        }
        binding?.rwPlayLists?.adapter = playListBottomSheetAdapter
    }

    private fun initClickDebounce() {
        onPlayListClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playList ->
            val format =
                if (playList.tracks?.contains(track) == true) R.string.already_added_to_play_list else R.string.added_to_play_list
            val message = playList.nameOfAlbum ?: ""
            if (playList.tracks?.contains(track) == true) {
                Toast.makeText(requireContext(), getString(format, message), Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.addTrackToPlayList(track, playList)
                showSnackBar(format, message)
                dialog?.cancel()
            }
        }
    }

    private fun startPlayListCreateFragment() {
        findNavController()
            .navigate(
                R.id.action_playListBottomSheetFragment_to_playListCreateFragment
            )
    }

    private fun observeFlow() {
        viewModel.getPlayListSharedFlow()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { playListState ->
                when (playListState) {
                    is PlayListState.Content -> {
                        showContent(playListState.playLists)
                    }

                    PlayListState.Empty -> {
                        showEmpty()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showContent(playLists: List<PlayListCreateData>) {
        binding?.let {
            playListBottomSheetAdapter?.submitList(playLists)
            it.rwPlayLists.isVisible = true
            it.imageViewNoData.isVisible = false
            it.textViewNoData.isVisible = false
        }
    }

    private fun showEmpty() {
        binding?.let {
            it.rwPlayLists.isVisible = false
            it.imageViewNoData.isVisible = true
            it.textViewNoData.isVisible = true
        }
    }

    private fun showSnackBar(@StringRes format: Int, albumName: String) {
        val rootView = requireActivity().findViewById<FragmentContainerView>(R.id.container_view)
        val message = getString(format, albumName)
        val (textColor, backgroundColor) = if ((requireContext().applicationContext as App).darkTheme) {
            ContextCompat.getColor(
                requireContext(),
                R.color.dark_grey
            ) to ContextCompat.getColor(requireContext(), R.color.deep_white)
        } else {
            ContextCompat.getColor(
                requireContext(),
                R.color.deep_white
            ) to ContextCompat.getColor(requireContext(), R.color.dark_grey)
        }
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
            .setTextColor(textColor)
            .setBackgroundTint(backgroundColor)
            .show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        private const val EXTRA_TRACK = "extra_track"

        fun createArgs(track: Track): Bundle {
            val jsonString = Json.encodeToString(track)
            return bundleOf(EXTRA_TRACK to jsonString)
        }
    }
}