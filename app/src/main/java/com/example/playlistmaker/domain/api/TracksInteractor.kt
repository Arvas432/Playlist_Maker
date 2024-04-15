package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TracksSearchResult

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(searchResult: TracksSearchResult)
    }
}