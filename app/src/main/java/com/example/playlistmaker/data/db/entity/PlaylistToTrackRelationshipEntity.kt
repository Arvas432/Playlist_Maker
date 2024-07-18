package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "track_id"], tableName = "playlist_relationship_table")
data class PlaylistToTrackRelationshipEntity(
    val playlistId: Long,
    val track_id: Long,
    @ColumnInfo(name = "insertion_timestamp")
    val insertionTimestamp: Long = System.currentTimeMillis()
)
