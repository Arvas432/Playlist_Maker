package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryInteractor {
    fun write(input: Track)
    fun clear()
    fun read(): List<Track>
}