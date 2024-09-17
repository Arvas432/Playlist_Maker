package com.example.playlistmaker.domain.player

interface MediaPlayerRepository{
    fun preparePlayer(url: String, action: () -> Unit)
    fun play()
    fun pause()
    fun releasePlayer()
    fun getPosition(): Int
    fun getPlayerState(): Int
    fun setCompletionListener(action: () -> Unit)
}