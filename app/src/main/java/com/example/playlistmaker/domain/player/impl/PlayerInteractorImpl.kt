package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) : PlayerInteractor {
    private val playerTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    override fun preparePlayer(url: String, action: () -> Unit) {
        mediaPlayerRepository.preparePlayer(url, action)
    }

    override fun play() {
        mediaPlayerRepository.play()
    }

    override fun pause() {
        mediaPlayerRepository.pause()
    }

    override fun releasePlayer() {
        mediaPlayerRepository.releasePlayer()
    }
    override fun getCurrentPosFormatted(): String {
        return playerTimeFormat.format(mediaPlayerRepository.getPosition())
    }

    override fun getPlayerState(): Int {
        return mediaPlayerRepository.getPlayerState()
    }

    override fun setCompletionListener(action: () -> Unit) {
        mediaPlayerRepository.setCompletionListener {action()  }
    }
}