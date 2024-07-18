package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.entity.PlaylistToTrackRelationshipEntity
import com.example.playlistmaker.data.db.entity.PlaylistsTrackPoolEntity
import com.example.playlistmaker.data.db.entity.TrackWithPlaylists

@Dao
interface PlaylistPoolDao {
    @Query("SELECT * FROM playlists_track_pool_table WHERE track_id = :id")
    suspend fun getPlaylistsPoolTrackById(id: Int): PlaylistsTrackPoolEntity
    @Insert(entity = PlaylistsTrackPoolEntity::class,OnConflictStrategy.REPLACE)
    suspend fun insertNewTrack(track: PlaylistsTrackPoolEntity)
    @Query("DELETE FROM playlists_track_pool_table WHERE track_id =:id")
    suspend fun deleteTrackFromPool(id: Int)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrackRelationship(relationship: PlaylistToTrackRelationshipEntity): Long
    @Query("DELETE FROM playlist_relationship_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylistTrackRelationships(playlistId: Long)
    @Query("DELETE FROM playlist_relationship_table WHERE playlistId = :playlistId AND track_id = :trackId")
    suspend fun deleteOneTrackRelationship(playlistId: Long, trackId: Int)
    @Query("SELECT COUNT(*) FROM playlist_relationship_table WHERE track_id = :trackId")
    suspend fun getPlaylistCountForTrack(trackId: Int): Int
    @Query("SELECT COUNT(*) FROM playlist_relationship_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistTrackCount(playlistId: Long): Int
    @Query("SELECT track_id FROM playlist_relationship_table WHERE playlistId = :playlistId ORDER BY insertion_timestamp")
    suspend fun getTrackIdsForPlaylist(playlistId: Long): List<Int>

}