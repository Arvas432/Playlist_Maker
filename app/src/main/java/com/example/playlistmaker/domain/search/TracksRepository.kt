package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.ServerResponseType
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Pair<List<Track>,ServerResponseType>>
}