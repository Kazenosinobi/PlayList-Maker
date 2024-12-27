package com.practicum.playlistmaker.basePlayList.playListCreate.ui

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.basePlayList.ui.BasePlayListFragment
import com.practicum.playlistmaker.core.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListCreateFragment : BasePlayListFragment() {

    override val viewModel: PlayListCreateViewModel by viewModel()

    override fun checkBeforeCloseScreen() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.PlayListCreateFragmentDialogTitle)
            .setMessage(R.string.PlayListCreateFragmentDialogMessage)
            .setNeutralButton(R.string.Cancel) { _, _ ->

            }
            .setPositiveButton(R.string.Complete) { _, _ ->
                findNavController().navigateUp()
            }
            .show()

    }

    override fun getPlayList() {

        val nameOfAlbum = binding?.editTextName?.text.toString()
        val descriptionOfAlbum = binding?.editTextDescription?.text.toString()
        val image = super.imagePath.toString()

        viewModel.savePlayList(image, nameOfAlbum, descriptionOfAlbum)

        val rootView = requireActivity().findViewById<FragmentContainerView>(R.id.container_view)
        val message =
            getString(R.string.play_list_created, binding?.editTextName?.text?.toString())
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
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setTextColor(textColor)
            .setBackgroundTint(backgroundColor)
            .setAnchorView(requireActivity().findViewById(R.id.bottomNavigationView))
            .show()


        findNavController().navigateUp()
    }

    override fun closeScreen() {

        val isNameSet = binding?.editTextName?.text.isNullOrBlank().not()
        val isDescriptionSet = binding?.editTextDescription?.text.isNullOrBlank().not()
        if (imagePath != null || isNameSet || isDescriptionSet) {
            checkBeforeCloseScreen()
        } else {
            findNavController().navigateUp()
        }

    }
}