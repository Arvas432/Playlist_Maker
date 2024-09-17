package com.example.playlistmaker.ui.playlistInfo.states

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track

sealed class PlaylistInfoStates{
    data object Default: PlaylistInfoStates()
    data class EmptyPlaylist(val playlistObject: Playlist): PlaylistInfoStates()
    data class PlaylistContent(val playlistObject: Playlist, val tracks: List<Track>, val tracksLengthSum: Int): PlaylistInfoStates()
}
