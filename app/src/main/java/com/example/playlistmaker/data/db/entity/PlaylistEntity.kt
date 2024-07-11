package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    val name: String,
    val description: String,
    val imageFilePath: String?,
    val serializedTrackList: String,
    val trackCount: Int,
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0L,
)
