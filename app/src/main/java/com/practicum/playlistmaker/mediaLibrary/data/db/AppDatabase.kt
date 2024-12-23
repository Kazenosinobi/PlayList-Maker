package com.practicum.playlistmaker.mediaLibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.mediaLibrary.data.db.dao.TrackDao
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.playListCreate.data.db.dao.PlayListDao
import com.practicum.playlistmaker.playListCreate.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.playListCreate.data.db.entity.TrackListConverter

@Database(version = 9, entities = [TrackEntity::class, PlayListEntity::class])
@TypeConverters(TrackListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao

}