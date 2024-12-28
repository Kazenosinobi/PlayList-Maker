package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.basePlayList.playListEdit.ui.PlayListEditFragment
import com.practicum.playlistmaker.databinding.FragmentPlayListMenuBottomSheetBinding
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.playListScreen.ui.BottomSheetDimensions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListMenuBottomSheetFragment : BottomSheetDialogFragment() {

    private val playList by lazy {
        val jsonString = requireArguments().getString(EXTRA_PLAY_LIST) ?: ""
        Json.decodeFromString<PlayListData>(jsonString)
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
        setUpBottomSheetHeight()
    }

    private fun initListeners() {
        binding?.textViewShare?.setOnClickListener {
            startSharing()
        }

        binding?.textViewEdit?.setOnClickListener {
            startPlayListScreenFragment(playList.playListId.toInt())
        }

        binding?.textViewDelete?.setOnClickListener {
            showDeletePlayListDialog()
            dialog?.hide()
        }
    }

    private fun showContent(playList: PlayListData) {
        setText(playList)
        getImageAlbum(playList)
    }

    private fun getImageAlbum(playList: PlayListData) {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        binding?.let {

            Glide.with(this)
                .load(playList.image)
                .placeholder(R.drawable.place_holder)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(cornerRadius)
                    )
                )
                .into(it.include.imageViewAlbum)

        }
    }

    private fun setText(playList: PlayListData) {

        binding?.let {
            it.include.TextViewName.text = playList.nameOfAlbum
            it.include.TextViewTracksCount.text = getTotalTracksText(playList)
        }

    }

    private fun getTotalTracksText(playList: PlayListData): String? {
        val resources = binding?.root?.context?.resources
        val tracksCount = playList.tracks.size
        return tracksCount.let {
            resources?.getQuantityString(
                R.plurals.tracks_count,
                it,
                tracksCount
            )
        }
    }

    private fun setUpBottomSheetHeight() {
        val bottomSheetDimensions = BottomSheetDimensions(requireActivity())
        binding?.llBottomSheet?.let {
            bottomSheetDimensions.setupBottomSheetHeightForDialogFragment(
                dialog as BottomSheetDialog,
                PERCENT_OF_BOTTOM_SHEET_HEIGHT,
            )
        }
    }

    private fun showDeletePlayListDialog() {
        val format = R.string.want_to_delete_a_play_list
        val message = playList.nameOfAlbum
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_play_list)
            .setMessage(getString(format, message))
            .setNeutralButton(R.string.no) { _, _ ->

            }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deletePlayList(playList.playListId.toInt())
                findNavController().popBackStack(R.id.mediaLibraryFragment, false)
            }
            .show()
    }

    private fun startSharing() {
        if (playList.tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                R.string.no_tracks,
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        } else {
            viewModel.sharePlayList(playList)
        }
    }

    private fun startPlayListScreenFragment(playListId: Int) {
        findNavController()
            .navigate(
                R.id.action_playListMenuBottomSheetFragment2_to_playListEditFragment,
                PlayListEditFragment.createArgs(playListId)
            )
    }

    companion object {
        private const val EXTRA_PLAY_LIST = "extra_play_list"
        private const val PERCENT_OF_BOTTOM_SHEET_HEIGHT = 0.37f

        fun createArgs(playList: PlayListData): Bundle {
            val jsonString = Json.encodeToString(playList)
            return bundleOf(EXTRA_PLAY_LIST to jsonString)
        }
    }
}