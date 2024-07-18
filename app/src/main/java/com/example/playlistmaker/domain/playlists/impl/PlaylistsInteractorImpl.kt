package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository):PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists().map { list -> list.reversed() }
    }

    override fun getPlaylistById(id: Long): Flow<Playlist> {
        return repository.getPlaylistById(id)
    }

    override fun getTracksByPlaylistId(id: Long): Flow<List<Track>> {
        return repository.getTracksByPlaylistId(id).map { list -> list.reversed() }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        repository.deletePlaylistAndTracks(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int) {
        repository.removeTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean> {
        return repository.addTrackToPlaylist(track, playlist)
    }
}