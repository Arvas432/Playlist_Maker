package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.playlists.models.PlaylistAdditionResultType
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getTracksFromListIds(ids: List<Int>): Flow<List<Track>>
    suspend fun addTrack(track: Track)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<PlaylistAdditionResultType>
}