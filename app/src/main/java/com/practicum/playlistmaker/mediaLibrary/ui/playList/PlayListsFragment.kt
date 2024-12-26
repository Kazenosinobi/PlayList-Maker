package com.practicum.playlistmaker.mediaLibrary.ui.playList

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
import com.practicum.playlistmaker.databinding.FragmentPlayListsBinding
import com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler.GridSpacingItemDecoration
import com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler.PlayListAdapter
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListScreen.ui.PlayListScreenFragment
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {

    private val viewModel: PlayListViewModel by viewModel()

    private var binding: FragmentPlayListsBinding? = null

    private var playListAdapter: PlayListAdapter? = null

    private var onPlayListClickDebounce: ((PlayListCreateData) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initAdapters()
        initClickDebounce()
        observeFlow()
        albumImageDecoration()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playListAdapter = null
    }

    private fun initListeners() {
        binding?.newPlaylistButton?.setOnClickListener {
            startPlayListCreateFragment()
        }
    }

    private fun initAdapters() {
        playListAdapter = PlayListAdapter { playList ->
            onPlayListClickDebounce?.let { it(playList) }
        }
        binding?.rwPlayLists?.adapter = playListAdapter
    }

    private fun initClickDebounce() {
        onPlayListClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playList ->
            startPlayListScreenFragment(playList.playListId.toInt())
        }
    }

    private fun startPlayListCreateFragment() {
        findNavController()
            .navigate(
                R.id.action_mediaLibraryFragment_to_playListCreateFragment
            )
    }

    private fun startPlayListScreenFragment(playListId: Int) {
        findNavController()
            .navigate(
                R.id.action_mediaLibraryFragment_to_playListScreenFragment,
                PlayListScreenFragment.createArgs(playListId)
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
            playListAdapter?.submitList(playLists)
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

    private fun albumImageDecoration() {
        binding?.rwPlayLists?.addItemDecoration(GridSpacingItemDecoration(requireContext()))
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        fun newInstance() = PlayListsFragment()
    }

}