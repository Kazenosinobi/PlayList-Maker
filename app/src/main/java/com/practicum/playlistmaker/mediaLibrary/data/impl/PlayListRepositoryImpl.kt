package com.practicum.playlistmaker.mediaLibrary.data.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.mapToPlayListCreateData
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListRepository
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.mediaLibrary.domain.models.mapToPlayListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase,
) : PlayListRepository {

    override suspend fun addPlayList(playList: PlayListData) {
        appDatabase.playListDao().addPlayList(playList.mapToPlayListEntity())
    }

    override suspend fun deletePlayList(playListId: Int) {
        appDatabase.playListDao().deletePlayList(playListId)
    }

    override suspend fun updatePlayList(playList: PlayListData) {
        appDatabase.playListDao().updatePlayList(playList.mapToPlayListEntity())
    }

    override fun getPlayList(): Flow<List<PlayListData>> =
        appDatabase.playListDao().getPlayList()
            .map { playListsEntity ->
                playListsEntity.map { playListEntity ->
                    playListEntity.mapToPlayListCreateData()
                }
            }

    override fun getPlayListById(playListId: Int): Flow<PlayListData> =
        appDatabase.playListDao().getPlayListById(playListId)
            .map { playList ->
                playList.mapToPlayListCreateData()
            }
            .catch { }

    override fun share(playList: PlayListData) {
        val message = formatPlaylistMessage(playList)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(intent, context.getString(R.string.messenger_choose))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun formatPlaylistMessage(playList: PlayListData): String {
        val tracksCountText = playList.tracks.let {
            context.resources.getQuantityString(
                R.plurals.tracks_count,
                it.size,
                playList.tracks.size
            )
        }

        val tracksInfo = playList.tracks.mapIndexed { index, track ->
            val durationMinutes = track.trackTimeMillis / MILLIS_IN_MINUTE
            val durationSeconds = (track.trackTimeMillis % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND
            val formattedDuration = String.format("%d:%02d", durationMinutes, durationSeconds)
            "${index + 1}. ${track.artistName} - ${track.trackName} ($formattedDuration)"
        }.joinToString("\n")

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