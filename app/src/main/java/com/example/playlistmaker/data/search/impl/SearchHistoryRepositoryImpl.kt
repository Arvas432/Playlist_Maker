package com.example.playlistmaker.data.search.impl
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.LocalTrackStorageHandler
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(private val localTrackStorageHandler: LocalTrackStorageHandler, private val appDatabase: AppDatabase): SearchHistoryRepository {
    override fun write(input: Track) {
       localTrackStorageHandler.write(input)
    }

    override fun clear() {
        localTrackStorageHandler.clear()
    }

    override fun read(): Flow<List<Track>> = flow{
        val history = localTrackStorageHandler.read()
        val favoriteIds = appDatabase.favoritesDao().getFavoritesIds().toSet()
        emit(history.map { track -> track.apply { isFavorite = favoriteIds.contains(trackId) } })
    }
}