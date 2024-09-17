package com.example.playlistmaker.ui.mediateka.states

import com.example.playlistmaker.domain.playlists.models.Playlist

sealed class PlaylistsState{
    data object Default: PlaylistsState()
    data object Empty: PlaylistsState()
    data class Content(val playlists: List<Playlist>) : PlaylistsState()
}
