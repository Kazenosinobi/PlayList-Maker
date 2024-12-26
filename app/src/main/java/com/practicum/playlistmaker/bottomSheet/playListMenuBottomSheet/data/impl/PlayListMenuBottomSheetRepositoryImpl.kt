package com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.data.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.bottomSheet.playListMenuBottomSheet.domain.api.PlayListMenuBottomSheetRepository
import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import com.practicum.playlistmaker.basePlayList.domain.models.mapToPlayListEntity

class PlayListMenuBottomSheetRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase,
) : PlayListMenuBottomSheetRepository {

    override fun share(playList: PlayListCreateData) {
        val message = formatPlaylistMessage(playList)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(intent, context.getString(R.string.messenger_choose))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    override suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().deletePlayList(playList.mapToPlayListEntity())
    }

    private fun formatPlaylistMessage(playList: PlayListCreateData): String {
        val tracksCountText = playList.tracks?.let {
            context.resources.getQuantityString(
                R.plurals.tracks_count,
                it.size,
                playList.tracks.size
            )
        }

        val tracksInfo = playList.tracks?.mapIndexed { index, track ->
            val durationMinutes = track.trackTimeMillis / MILLIS_IN_MINUTE
            val durationSeconds = (track.trackTimeMillis % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND
            val formattedDuration = String.format("%d:%02d", durationMinutes, durationSeconds)
            "${index + 1}. ${track.artistName} - ${track.trackName} ($formattedDuration)"
        }?.joinToString("\n")

        return buildString {
            append(playList.nameOfAlbum).append("\n")
            append(playList.descriptionOfAlbum ?: "").append("\n")
            append(tracksCountText).append("\n")
            append(tracksInfo)
        }
    }

    private companion object {
        private const val MILLIS_IN_MINUTE = 60_000
        private const val MILLIS_IN_SECOND = 1_000
    }
}
