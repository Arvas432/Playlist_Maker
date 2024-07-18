package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoritesDao
import com.example.playlistmaker.data.db.dao.PlaylistPoolDao
import com.example.playlistmaker.data.db.dao.PlaylistsDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistToTrackRelationshipEntity
import com.example.playlistmaker.data.db.entity.PlaylistsTrackPoolEntity
import com.example.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistsTrackPoolEntity::class, PlaylistToTrackRelationshipEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoritesDao(): FavoritesDao
    abstract fun playlistsDao(): PlaylistsDao
    abstract fun playlistsPoolDao(): PlaylistPoolDao
}