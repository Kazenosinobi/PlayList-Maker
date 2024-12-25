package com.practicum.playlistmaker.media.ui

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.bottomSheet.playListBottomSheet.ui.PlayListBottomSheetFragment
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaFragment : Fragment() {
    private var binding: FragmentMediaBinding? = null

    private val track by lazy {
        val jsonString = requireArguments().getString(EXTRA_TRACK) ?: ""
        Json.decodeFromString<Track>(jsonString)
    }

    private val viewModel by viewModel<MediaViewModel> {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getImageAlbum()
        setText()
        observeFlow()
        initListeners()
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun observeFlow() {
        viewModel.getPlayerStateFlow()
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                renderState(state)
            }
            .launchIn(lifecycleScope)
    }

    private fun initListeners() {
        binding?.backButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        binding?.imageViewPlay?.setOnClickListener {
            viewModel.playbackControl(track.trackUrl ?: "")
        }

        binding?.imageViewFavourite?.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding?.imageViewCatalog?.setOnClickListener {
            startPlayListBottomSheetFragment(track)
        }
    }

    private fun updateFavouriteButton(isFavourite: Boolean) {
        val icon = if (isFavourite) R.drawable.added_to_favourite else R.drawable.favourite
        binding?.imageViewFavourite?.setImageResource(icon)
    }

    private fun showToast(message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun renderState(state: PlayerStateData) {
        binding?.let { binding ->
            with(binding) {
                textViewPlayTime.text = state.currentPosition
                imageViewPlay.isEnabled = state.playerState != PlayerState.STATE_DEFAULT
                imageViewPlay.imageAlpha =
                    if (state.playerState == PlayerState.STATE_DEFAULT) DISABLED_ALFA else ENABLED_ALFA

                when (state.playerState) {
                    PlayerState.STATE_DEFAULT -> {
                        val url = track.trackUrl
                        if (url.isNullOrBlank()) {
                            showToast(R.string.play_error)
                        } else {
                            viewModel.preparePlayer(url)
                        }
                    }

                    PlayerState.STATE_PREPARED -> {
                        imageViewPlay.setImageResource(R.drawable.play_button)
                        textViewPlayTime.text = START_TIME
                    }

                    PlayerState.STATE_PLAYING -> {
                        imageViewPlay.setImageResource(R.drawable.pause_button)
                    }

                    PlayerState.STATE_PAUSED -> {
                        imageViewPlay.setImageResource(R.drawable.play_button)
                    }

                    PlayerState.STATE_CONNECTION_ERROR -> {
                        showToast(R.string.connection_error_toast)
                    }
                }
            }

            updateFavouriteButton(state.isFavourite)
        }
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        binding?.let {
            Glide.with(this)
                .load(track.coverArtworkMaxi)
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(cornerRadius))
                .into(it.imageViewAlbum)
        }
    }

    private fun setText() {
        binding.let {
            it?.textViewTrackName?.text = track.trackName
            it?.textViewArtistName?.text = track.artistName
            it?.textViewDurationData?.text = getTrackTime()
            it?.textViewAlbumData?.text = getCollectionName()
            it?.textViewYearData?.text = getReleaseDate()
            it?.textViewGenreData?.text = getPrimaryGenreName()
            it?.textViewCountryData?.text = getCountry()
            it?.textViewPlayTime?.text = START_TIME
        }
    }

    private fun getCollectionName(): String {
        return track.collectionName ?: run {
            binding.let {
                it?.textViewAlbum?.isVisible = false
                it?.textViewAlbumData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getPrimaryGenreName(): String {
        return track.primaryGenreName ?: run {
            binding.let {
                it?.textViewGenre?.isVisible = false
                it?.textViewGenreData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getCountry(): String {
        return track.country ?: run {
            binding.let {
                it?.textViewCountry?.isVisible = false
                it?.textViewCountryData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getReleaseDate(): String {
        return track.releaseYear ?: run {
            binding.let {
                it?.textViewYear?.isVisible = false
                it?.textViewYearData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getTrackTime(): String {
        return track.getTrackTime() ?: run {
            binding.let {
                it?.textViewDuration?.isVisible = false
                it?.textViewDurationData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun startPlayListBottomSheetFragment(track: Track) {
        findNavController()
            .navigate(
                R.id.action_mediaFragment_to_playListBottomSheetFragment,
                PlayListBottomSheetFragment.createArgs(track)
            )
    }

    companion object {
        private const val START_TIME = "00:00"
        private const val EXTRA_TRACK = "extra_track"
        private const val EMPTY_STRING = ""
        private const val DISABLED_ALFA = 50
        private const val ENABLED_ALFA = 255

        fun createArgs(track: Track): Bundle {
            val jsonString = Json.encodeToString(track)
            return bundleOf(EXTRA_TRACK to jsonString)
        }
    }
}