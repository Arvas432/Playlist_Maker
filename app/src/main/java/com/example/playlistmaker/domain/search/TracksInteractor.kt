package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.TracksSearchResult
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<TracksSearchResult>
}