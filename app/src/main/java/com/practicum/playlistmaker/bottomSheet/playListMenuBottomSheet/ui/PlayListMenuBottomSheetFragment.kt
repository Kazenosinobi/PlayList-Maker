package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListMenuBottomSheetBinding
import com.practicum.playlistmaker.mediaLibrary.ui.playList.PlayListState
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListScreen.ui.BottomSheetDimensions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListMenuBottomSheetFragment : BottomSheetDialogFragment() {

    private val playList by lazy {
        val jsonString = requireArguments().getString(EXTRA_PLAY_LIST) ?: ""
        Json.decodeFromString<PlayListCreateData>(jsonString)
    }

    private val viewModel: PlayListMenuBottomSheetViewModel by viewModel()

    private var binding: FragmentPlayListMenuBottomSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListMenuBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        showContent(playList)
//        setUpBottomSheetHeight()
    }

    private fun initListeners() {
        binding?.textViewShare?.setOnClickListener {
            startSharing()
        }

        binding?.textViewEdit?.setOnClickListener {

        }

        binding?.textViewDelete?.setOnClickListener {
            showDeletePlayListDialog()
        }
    }

    private fun showContent(playList: PlayListCreateData) {
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
                .into(it.include.imageViewAlbum)

        }
    }

    private fun setText(playList: PlayListCreateData) {

        binding?.let {
            it.include.TextViewName.text = playList.nameOfAlbum
            it.include.TextViewTracksCount.text = getTotalTracksText(playList)
        }

    }

    private fun getTotalTracksText(playList: PlayListCreateData): String? {
        val resources = binding?.root?.context?.resources
        val tracksCount = playList.tracks?.size
        return tracksCount?.let {
            resources?.getQuantityString(
                R.plurals.tracks_count,
                it,
                tracksCount
            )
        }
    }

    private fun setUpBottomSheetHeight() {
        val bottomSheetDimensions = BottomSheetDimensions(requireActivity())
        binding?.llBottomSheet?.let { container ->
            bottomSheetDimensions.setupBottomSheetHeight(
                container,
                PERCENT_OF_BOTTOM_SHEET_HEIGHT
            )
        }
    }

    private fun showDeletePlayListDialog() {
        val format = R.string.want_to_delete_a_play_list
        val message = playList.nameOfAlbum
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(format, message))
            .setNeutralButton(R.string.no) { dialog, which ->

            }
            .setPositiveButton(R.string.yes) { dialog, which ->
                viewModel.deletePlayList(playList)
                findNavController().popBackStack(R.id.mediaLibraryFragment, false)
            }
            .show()
    }

    private fun startSharing() {
        if (playList.tracks?.isEmpty() == true) {
            Toast.makeText(
                requireContext(),
                R.string.no_tracks,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.sharePlayList(playList)
        }
    }

    companion object {
        private const val EXTRA_PLAY_LIST = "extra_play_list"
        private const val PERCENT_OF_BOTTOM_SHEET_HEIGHT = 0.37f

        fun createArgs(playList: PlayListCreateData): Bundle {
            val jsonString = Json.encodeToString(playList)
            return bundleOf(EXTRA_PLAY_LIST to jsonString)
        }
    }
}