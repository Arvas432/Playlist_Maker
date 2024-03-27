package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.MediaPlayerHandler
import com.example.playlistmaker.domain.api.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(override val mediaPlayerHandler: MediaPlayerHandler) : PlayerInteractor {
    private val playerTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    override fun preparePlayer(url: String) {
        mediaPlayerHandler.preparePlayer(url)
    }

    override fun play() {
        mediaPlayerHandler.play()
    }

    override fun pause() {
        mediaPlayerHandler.pause()
    }

    override fun releasePlayer() {
        mediaPlayerHandler.releasePlayer()
    }
    override fun getCurrentPosFormatted(): String {
        return playerTimeFormat.format(mediaPlayerHandler.getPosition())
    }

    override fun getPlayerState(): Int {
        return mediaPlayerHandler.getPlayerState()
    }

    override fun setCompletionListener(action: () -> Unit) {
        mediaPlayerHandler.setCompletionListener {action()  }
    }
}