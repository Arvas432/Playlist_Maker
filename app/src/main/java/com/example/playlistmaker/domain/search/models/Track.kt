package com.example.playlistmaker.domain.search.models

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val formattedDuration: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
)
