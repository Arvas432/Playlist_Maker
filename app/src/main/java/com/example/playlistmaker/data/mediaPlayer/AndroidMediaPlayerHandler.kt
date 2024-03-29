package com.example.playlistmaker.data.mediaPlayer

import android.media.MediaPlayer
import com.example.playlistmaker.data.MediaPlayerHandler

class AndroidMediaPlayerHandler: MediaPlayerHandler {
    private var mediaPlayer = MediaPlayer()
    private var playerState: Int = PLAYER_STATE_DEFAULT
    override fun preparePlayer(url: String){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PLAYER_STATE_PREPARED
        }
    }
    override fun play(){
        mediaPlayer.start()
        playerState = PLAYER_STATE_PLAYING
    }
    override fun pause(){
        mediaPlayer.pause()
        playerState = PLAYER_STATE_PAUSED
    }
    override fun releasePlayer(){
        mediaPlayer.release()
    }
    override fun getPosition(): Int{
        return mediaPlayer.currentPosition
    }
    override fun getPlayerState(): Int{
        return playerState
    }

    override fun setCompletionListener(action: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = PLAYER_STATE_PREPARED
            action()
        }
    }
    companion object{
        const val PLAYER_STATE_DEFAULT = 0
        const val PLAYER_STATE_PREPARED = 1
        const val PLAYER_STATE_PLAYING = 2
        const val PLAYER_STATE_PAUSED = 3
    }
}
