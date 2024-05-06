package com.example.playlistmaker.data.search.impl
import com.example.playlistmaker.data.search.LocalTrackStorageHandler
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchHistoryRepositoryImpl(private val localTrackStorageHandler: LocalTrackStorageHandler): SearchHistoryRepository {
    override fun write(input: Track) {
       localTrackStorageHandler.write(input)
    }

    override fun clear() {
        localTrackStorageHandler.clear()
    }

    override fun read(): List<Track> {
        return localTrackStorageHandler.read()
    }
}