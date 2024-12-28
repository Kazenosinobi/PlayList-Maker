package com.practicum.playlistmaker.mediaLibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackForPlayListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackForPlayListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TrackForPlayListEntity)

    @Delete
    suspend fun deleteTrackFromDB(track: TrackForPlayListEntity)

    @Query("SELECT * FROM track_table_for_play_list")
    fun getTracksFromDBFlow(): Flow<List<TrackForPlayListEntity>>

    @Query("SELECT * FROM track_table_for_play_list")
    suspend fun getTracksFromDB(): List<TrackForPlayListEntity>
}