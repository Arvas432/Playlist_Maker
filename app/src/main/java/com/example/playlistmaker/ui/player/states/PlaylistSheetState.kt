package com.example.playlistmaker.ui.player.states

import com.example.playlistmaker.domain.playlists.models.Playlist

sealed class PlaylistSheetState{
    data object Default: PlaylistSheetState()
    data class Content(val playlists: List<Playlist>): PlaylistSheetState()
    data class TrackAlreadyAdded(val playlistName: String): PlaylistSheetState()
    data class TrackAdded(val playlistName: String): PlaylistSheetState()
}
