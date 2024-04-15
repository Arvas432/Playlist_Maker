package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.MediaPlayerHandler

interface PlayerInteractor {
    val mediaPlayerHandler: MediaPlayerHandler
    fun preparePlayer(url: String)
    fun play()
    fun pause()
    fun getCurrentPosFormatted(): String
    fun getPlayerState(): Int
    fun releasePlayer()
    fun setCompletionListener(action: () -> Unit)

}