package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository): SearchHistoryInteractor {
    override fun write(input: Track) {
        repository.write(input)
    }

    override fun clear() {
        repository.clear()
    }

    override fun read(): List<Track> {
        return repository.read()
    }
}