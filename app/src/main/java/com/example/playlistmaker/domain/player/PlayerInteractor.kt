package com.example.playlistmaker.domain.player

interface PlayerInteractor {
    fun preparePlayer(url: String, action: () -> Unit)
    fun play()
    fun pause()
    fun getCurrentPosFormatted(): String
    fun getPlayerState(): Int
    fun releasePlayer()
    fun setCompletionListener(action: () -> Unit)

}