package com.practicum.playlistmaker.playListCreate.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.playListCreate.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Delete
    suspend fun deletePlayList(playList: PlayListEntity)

    @Update
    suspend fun updatePlayList(playList: PlayListEntity)

    @Query("SELECT playListId, playListName, playListDescription, imagePath, tracks FROM play_list_table")
    fun getPlayList(): Flow<List<PlayListEntity>>
}