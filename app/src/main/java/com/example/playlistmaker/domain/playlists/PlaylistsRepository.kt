package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylistById(id: Long): Flow<Playlist>
    fun getTracksByPlaylistId(playlistId: Long): Flow<List<Track>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylistAndTracks(playlistId: Long)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrack(track: Track)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean>
}