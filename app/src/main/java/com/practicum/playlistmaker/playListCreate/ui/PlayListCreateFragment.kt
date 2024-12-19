package com.practicum.playlistmaker.playListCreate.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.databinding.FragmentPlayListCreateBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlayListCreateFragment : Fragment() {

    private var binding: FragmentPlayListCreateBinding? = null

    private val viewModel: PlayListCreateViewModel by viewModel()

    private val requester = PermissionRequester.instance()

    private var imagePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayListCreateBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        dataCompleteness()
    }

    private val picMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            imagePath = uri
            if (uri != null) {

                binding?.imageViewAddPic?.let { image ->
                    val cornerRadius =
                        binding?.root?.context?.resources?.getDimensionPixelSize(R.dimen._8dp)
                    Glide.with(requireContext())
                        .load(uri)
                        .placeholder(R.drawable.place_holder)
                        .fitCenter()
                        .transform(cornerRadius?.let { corners -> RoundedCorners(corners) })
                        .into(image)
                }

                saveImageToPrivateStorage(uri)
            }
        }

    private fun initListeners() {
        binding?.backButton?.setOnClickListener {
            val isNameSet = binding?.editTextName?.text.isNullOrBlank().not()
            val isDescriptionSet = binding?.editTextDescription?.text.isNullOrBlank().not()
            if (imagePath != null || isNameSet || isDescriptionSet) {
                showDialog()
            } else {
                findNavController().navigateUp()
            }
        }

        binding?.editTextName?.doAfterTextChanged {
            dataCompleteness()
        }

        binding?.buttonCreate?.setOnClickListener {
            val nameOfAlbum = binding?.editTextName?.text.toString()
            val descriptionOfAlbum = binding?.editTextDescription?.text.toString()
            val image = imagePath.toString()

            viewModel.savePlayList(image, nameOfAlbum, descriptionOfAlbum)

            val rootView = requireActivity().window.decorView.rootView
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
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setAnchorView(binding?.buttonCreate)
                .setTextColor(textColor)
                .setBackgroundTint(backgroundColor)
                .show()


            findNavController().navigateUp()
        }

        binding?.imageViewAddPic?.setOnClickListener {
            readMediaImagesPermission()
        }
    }

    private fun dataCompleteness() {
        val isNameNotEmpty = !binding?.editTextName?.text.isNullOrBlank()
        binding?.buttonCreate?.isEnabled = isNameNotEmpty
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            NAME_OF_FOLDER
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, CHILD_PATH)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun readMediaImagesPermission() {
        lifecycleScope.launch {
            requester.request(android.Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                when (result) {
                    is PermissionResult.Granted -> {
                        picMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                    is PermissionResult.Denied.DeniedPermanently -> {
                        val intent =
                            Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                data =
                                    Uri.fromParts(SCHEME, requireContext().packageName, null)
                            }
                        startActivity(intent)
                    }

                    is PermissionResult.Denied.NeedsRationale -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.DescriptionOfPermission,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is PermissionResult.Cancelled -> {
                        return@collect
                    }
                }
            }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.PlayListCreateFragmentDialogTitle)
            .setMessage(R.string.PlayListCreateFragmentDialogMessage)
            .setNeutralButton(R.string.Cancel) { dialog, which ->

            }
            .setPositiveButton(R.string.No) { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

    private companion object {
        private const val NAME_OF_FOLDER = "Play list maker album"
        private const val CHILD_PATH = "first_cover.jpg"
        private const val SCHEME = "package"
    }
}