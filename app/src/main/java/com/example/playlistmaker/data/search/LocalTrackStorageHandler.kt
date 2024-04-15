package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.models.Track

interface LocalTrackStorageHandler {
    fun write(input: Track)
    fun clear()
    fun read(): List<Track>
}