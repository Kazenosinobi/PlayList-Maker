package com.practicum.playlistmaker.basePlayList.playListEdit.ui

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import com.practicum.playlistmaker.basePlayList.ui.BasePlayListFragment
import com.practicum.playlistmaker.playListScreen.ui.PlayListScreenState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlayListEditFragment : BasePlayListFragment() {

    private val playListId by lazy {
        val jsonString = requireArguments().getString(EXTRA_PLAY_LIST_ID) ?: ""
        Json.decodeFromString<Int>(jsonString)
    }

    override val viewModel: PlayListEditViewModel by viewModel {
        parametersOf(playListId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFlow()
    }

    override fun getPlayList() {

        val nameOfAlbum = binding?.editTextName?.text.toString()
        val descriptionOfAlbum = binding?.editTextDescription?.text.toString()
        val imagePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), NAME_OF_FOLDER)
        val image = File(imagePath, CHILD_PATH)

        viewModel.updatePlayList(image.toString(), nameOfAlbum, descriptionOfAlbum)

        findNavController().navigateUp()
    }

    override fun closeScreen() {
        findNavController().navigateUp()
    }

    private fun showContent(playList: PlayListCreateData) {
        setText(playList.nameOfAlbum.orEmpty(), playList.descriptionOfAlbum)
        setImage(playList.image.orEmpty())
    }

    private fun setText(nameOfAlbum: String, descriptionOfAlbum: String?) {
        binding?.textViewTittle?.text = getString(R.string.edit)
        binding?.buttonCreate?.text = getString(R.string.save)
        binding?.editTextName?.setText(nameOfAlbum)
        binding?.editTextDescription?.setText(descriptionOfAlbum.orEmpty())
    }

    private fun setImage(url: String) {
        val cornerRadius =
            binding?.root?.context?.resources?.getDimensionPixelSize(R.dimen._8dp)
        binding?.imageViewAddPic?.let {
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(cornerRadius?.let { corners -> RoundedCorners(corners) })
                .into(it)
        }
    }

    private fun observeFlow() {
        viewModel.getPlayListEditStateFlow()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { playListScreenState ->
                when (playListScreenState) {
                    is PlayListScreenState.Content -> showContent(playListScreenState.playList)
                    PlayListScreenState.Empty -> Unit
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        private const val EXTRA_PLAY_LIST_ID = "extra_play_list_id"
        private const val NAME_OF_FOLDER = "Play list maker album"
        private const val CHILD_PATH = "first_cover.jpg"
        private const val SCHEME = "package"

        fun createArgs(playListId: Int): Bundle {
            val jsonString = Json.encodeToString(playListId)
            return bundleOf(EXTRA_PLAY_LIST_ID to jsonString)
        }
    }
}