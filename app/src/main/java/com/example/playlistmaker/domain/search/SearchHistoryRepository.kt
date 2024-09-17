package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun write(input: Track)
    fun clear()
    fun read(): Flow<List<Track>>
}