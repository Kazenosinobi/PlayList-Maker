package com.practicum.playlistmaker.mediaLibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.mediaLibrary.data.db.dao.PlayListDao
import com.practicum.playlistmaker.mediaLibrary.data.db.dao.TrackDao
import com.practicum.playlistmaker.mediaLibrary.data.db.dao.TrackForPlayListDao
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackForPlayListEntity
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackListConverter

@Database(
    version = 15,
    entities = [TrackEntity::class, PlayListEntity::class, TrackForPlayListEntity::class]
)
@TypeConverters(TrackListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackForPlayListDao(): TrackForPlayListDao
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao

}