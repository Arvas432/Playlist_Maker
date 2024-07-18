package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistsTrackPoolEntity

@Dao
interface PlaylistPoolDao {
    @Query("SELECT * FROM playlists_track_pool_table WHERE track_id = :id")
    suspend fun getPlaylistsPoolTrackById(id: Int): PlaylistsTrackPoolEntity
    @Insert(entity = PlaylistsTrackPoolEntity::class,OnConflictStrategy.IGNORE)
    suspend fun insertNewTrack(track: PlaylistsTrackPoolEntity)
}