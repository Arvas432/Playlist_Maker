package com.example.playlistmaker.data.dto

data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    var primaryGenreName: String?,
    val country: String,
    val previewUrl: String) {
}