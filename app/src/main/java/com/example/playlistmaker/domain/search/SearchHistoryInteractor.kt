package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    fun write(input: Track)
    fun clear()
    fun read(): Flow<List<Track>>
}