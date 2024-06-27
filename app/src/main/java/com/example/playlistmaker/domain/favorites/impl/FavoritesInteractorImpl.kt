package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.favorites.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesInteractorImpl(val repository: FavoritesRepository): FavoritesInteractor {
    override suspend fun addToFavorites(track: Track) {
        repository.addToFavorites(track)
    }

    override suspend fun removeFromFavorites(track: Track) {
        repository.removeFromFavorites(track)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return repository.getFavorites().map { list -> list.reversed() }
    }
}