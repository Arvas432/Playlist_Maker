package com.example.playlistmaker.domain.playlists.models

import android.net.Uri

data class Playlist(
    val name: String,
    val description: String,
    val imageFileUri: Uri?,
    val trackList: List<Int> = emptyList(),
    var trackCount: Int,
    val id: Long? = null
)
