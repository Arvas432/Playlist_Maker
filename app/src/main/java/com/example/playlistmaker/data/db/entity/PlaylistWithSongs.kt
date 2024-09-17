package com.example.playlistmaker.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "track_id",
        associateBy = Junction(PlaylistToTrackRelationshipEntity::class)
    )
    val tracks: List<PlaylistsTrackPoolEntity>
)

