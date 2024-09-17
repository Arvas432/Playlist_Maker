package com.example.playlistmaker.ui.player.states

sealed class PlayerFavoriteStatusState{
    data class FavoriteState(val isFavorite: Boolean): PlayerFavoriteStatusState()
    data object Default: PlayerFavoriteStatusState()
}
