package com.example.playlistmaker.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TrackWithPlaylists(
    @Embedded val song: PlaylistsTrackPoolEntity,
    @Relation(
        parentColumn = "track_id",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistToTrackRelationshipEntity::class)
    )
    val playlists: List<PlaylistEntity>
)

