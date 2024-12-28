package com.practicum.playlistmaker.basePlayList.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListBaseBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

abstract class BasePlayListFragment : Fragment() {

    protected var binding: FragmentPlayListBaseBinding? = null

    protected abstract val viewModel: BasePlayListViewModel

    private val requester = PermissionRequester.instance()

    protected var imagePath: Uri? = null

    private val picMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            imagePath = uri
            if (uri != null) {
                handleImagePicked(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayListBaseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        dataCompleteness()
    }

    protected open fun checkBeforeCloseScreen() = Unit

    protected open fun getPlayList() = Unit

    protected open fun closeScreen() = Unit

    private fun handleImagePicked(uri: Uri) {

        binding?.imageViewAddPic?.let { image ->
            val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.place_holder)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(cornerRadius)
                    )
                )
                .into(image)
        }
        saveImageToPrivateStorage(uri)

    }

    private fun dataCompleteness() {

        val isNameNotEmpty = !binding?.editTextName?.text.isNullOrBlank()
        binding?.buttonCreate?.isEnabled = isNameNotEmpty

    }

    private fun initListeners() {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding?.backButton?.setOnClickListener {
            closeScreen()
        }

        binding?.editTextName?.doAfterTextChanged {
            dataCompleteness()
        }

        binding?.buttonCreate?.setOnClickListener {
            getPlayList()
        }

        binding?.imageViewAddPic?.setOnClickListener {
            readMediaImagesPermission()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            NAME_OF_FOLDER
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.lastPathSegment ?: FILE_NAME)
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

    private companion object {
        private const val NAME_OF_FOLDER = "Play list maker album"
        private const val SCHEME = "package"
        private const val FILE_NAME = "image"
    }
}