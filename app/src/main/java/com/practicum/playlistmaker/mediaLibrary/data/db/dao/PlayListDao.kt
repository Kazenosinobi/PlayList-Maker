package com.practicum.playlistmaker.mediaLibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Query("DELETE FROM play_list_table WHERE playListId = :playListId")
    suspend fun deletePlayList(playListId: Int)

    @Update
    suspend fun updatePlayList(playList: PlayListEntity)

    @Query("SELECT * FROM play_list_table")
    fun getPlayList(): Flow<List<PlayListEntity>>

    @Query("SELECT * FROM play_list_table WHERE playListId = :playListId")
    fun getPlayListById(playListId: Int): Flow<PlayListEntity>
}