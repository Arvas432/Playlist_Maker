package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.SearchResultType
import com.example.playlistmaker.domain.search.models.TracksSearchResult
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.models.ServerResponseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(expression: String): Flow<TracksSearchResult> {
        return repository.searchTracks(expression).map { result ->
            when (result.second) {
                ServerResponseType.SUCCESS -> {
                    if (result.first.isNotEmpty()) {
                        TracksSearchResult(result.first, SearchResultType.SUCCESS)
                    } else {
                        TracksSearchResult(emptyList(), SearchResultType.EMPTY)
                    }
                }

                ServerResponseType.ERROR -> {
                    TracksSearchResult(emptyList(), SearchResultType.ERROR)
                }
            }
        }
    }

}