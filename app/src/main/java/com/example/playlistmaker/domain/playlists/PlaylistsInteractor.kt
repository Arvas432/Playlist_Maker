package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getPlaylistById(id: Long): Flow<Playlist>
    fun getTracksByPlaylistId(id: Long): Flow<List<Track>>
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int)
    suspend fun addTrack(track: Track)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean>
}