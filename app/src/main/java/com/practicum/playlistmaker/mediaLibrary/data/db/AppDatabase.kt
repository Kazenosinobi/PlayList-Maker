package com.practicum.playlistmaker.mediaLibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.mediaLibrary.data.db.dao.TrackDao
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.playListCreate.data.db.dao.PlayListDao
import com.practicum.playlistmaker.playListCreate.data.db.entity.PlayListEntity

@Database(version = 3, entities = [TrackEntity::class, PlayListEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao

}