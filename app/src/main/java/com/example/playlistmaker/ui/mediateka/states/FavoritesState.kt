package com.example.playlistmaker.ui.mediateka.states

import com.example.playlistmaker.domain.search.models.Track

sealed class FavoritesState{
    data object Default: FavoritesState()
    data object Empty: FavoritesState()
    data class Content(val tracks: List<Track>): FavoritesState()

}
