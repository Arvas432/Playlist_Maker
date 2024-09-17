package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_track_pool_table")
data class PlaylistsTrackPoolEntity(
    @PrimaryKey
    @ColumnInfo(name = "track_id")
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
)
