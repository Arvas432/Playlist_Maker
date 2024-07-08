package com.example.playlistmaker.ui.player

sealed class PlayerFavoriteStatusState{
    data class FavoriteState(val isFavorite: Boolean): PlayerFavoriteStatusState()
    data object Default: PlayerFavoriteStatusState()
}
