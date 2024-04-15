package com.example.playlistmaker.data

interface MediaPlayerHandler{
    fun preparePlayer(url: String)
    fun play()
    fun pause()
    fun releasePlayer()
    fun getPosition(): Int
    fun getPlayerState(): Int
    fun setCompletionListener(action: () -> Unit)
}