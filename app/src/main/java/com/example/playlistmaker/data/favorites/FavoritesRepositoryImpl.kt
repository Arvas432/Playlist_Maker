package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.favorites.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackConverter: TrackMapper
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        appDatabase.favoritesDao().insertTrackIntoFavorites(trackConverter.mapModelToEntity(track))
    }

    override suspend fun removeFromFavorites(track: Track) {
        appDatabase.favoritesDao().deleteTrackFromFavorites(track.trackId)
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
        val favorites = appDatabase.favoritesDao().getFavorites()
        emit(convertFromTrackEntity(favorites))
    }
    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map { track ->
            val output = trackConverter.mapEntityToModel(track).apply { isFavorite = true }
            output
        }
    }
}