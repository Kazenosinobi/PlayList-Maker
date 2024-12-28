package com.practicum.playlistmaker.mediaLibrary.data.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.mapToPlayListCreateData
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.mapToTrack
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListRepository
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.mediaLibrary.domain.models.mapToPlayListEntity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.mapToTrackForPlayListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase,
) : PlayListRepository {
    override suspend fun addTrackToPlayList(track: Track, playList: PlayListData) {
        appDatabase.trackForPlayListDao().addTrack(track.mapToTrackForPlayListEntity())
        appDatabase.playListDao()
            .updatePlayList(playList.addTrackId(track.trackId).mapToPlayListEntity())
    }

    override suspend fun removeTrackFromCurrentPlayList(track: Track, playList: PlayListData) {
        val newPlayList = playList.removeTrack(track)
        appDatabase.playListDao().updatePlayList(newPlayList.mapToPlayListEntity())
        if (isNeedToDeleteTrackFromDB(track.trackId)) {
            appDatabase.trackForPlayListDao()
                .deleteTrackFromDB(track.mapToTrackForPlayListEntity())
        }

    }

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

    override suspend fun share(playList: PlayListData) {
        val message = formatPlaylistMessage(playList)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(intent, context.getString(R.string.messenger_choose))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    override fun getTracksForCurrentPlayListFlow(tracksId: List<Int>): Flow<List<Track>> =
        appDatabase.trackForPlayListDao().getTracksFromDBFlow()
            .map { tracks ->
                tracks.filter { track ->
                    tracksId.contains(track.trackId)
                }
                    .map { track ->
                        track.mapToTrack()
                    }
            }

    private suspend fun getTracksForCurrentPlayList(tracksId: List<Int>): List<Track> {
        return appDatabase.trackForPlayListDao().getTracksFromDB()
            .filter { track ->
                tracksId.contains(track.trackId)
            }
            .map { track ->
                track.mapToTrack()
            }
    }


    private suspend fun isNeedToDeleteTrackFromDB(trackId: Int): Boolean {
        val tracksId = mutableListOf<Int>()
        appDatabase.playListDao().getPlayList()
            .collect { playLists ->
                playLists.map { playList ->
                    tracksId.addAll(playList.tracksId)
                }
            }
        return tracksId.contains(trackId).not()
    }

    private suspend fun formatPlaylistMessage(playList: PlayListData): String {
        val tracksCountText = playList.tracksId.let {
            context.resources.getQuantityString(
                R.plurals.tracks_count,
                it.size,
                playList.tracksId.size
            )
        }

        val tracks = getTracksForCurrentPlayList(playList.tracksId)
        val tracksInfo = tracks.mapIndexed { index, track ->
            val durationMinutes = track.trackTimeMillis / MILLIS_IN_MINUTE
            val durationSeconds =
                (track.trackTimeMillis % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND
            val formattedDuration =
                String.format("%d:%02d", durationMinutes, durationSeconds)
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