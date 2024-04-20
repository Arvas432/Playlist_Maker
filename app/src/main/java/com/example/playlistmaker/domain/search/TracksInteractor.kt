package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.TracksSearchResult

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(searchResult: TracksSearchResult)
    }
}