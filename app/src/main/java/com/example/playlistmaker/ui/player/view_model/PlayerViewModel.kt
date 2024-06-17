package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.player.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    private var currentPositionLiveData = MutableLiveData<String>()
    private var timerJob: Job? = null

    fun getScreenStateLiveData(): LiveData<PlayerState> = screenStateLiveData
    fun getCurrentPositionLiveData(): LiveData<String> = currentPositionLiveData
    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(
            track.previewUrl
        ) {
            renderState(PlayerState.Prepared)
        }
        playerInteractor.setCompletionListener {
            timerJob?.cancel()
            renderState(PlayerState.Prepared)
        }
    }

    fun startPlayer() {
        playerInteractor.play()
        renderState(PlayerState.Playing)
        timerJob = viewModelScope.launch {
            while (playerInteractor.getPlayerState() == AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PLAYING) {
                delay(TIMER_DELAY)
                currentPositionLiveData.postValue(playerInteractor.getCurrentPosFormatted())
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pause()
        renderState(PlayerState.Paused)
        timerJob?.cancel()
    }

    fun playbackControl() {
        when (playerInteractor.getPlayerState()) {
            AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PLAYING -> {
                pausePlayer()
            }

            AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PREPARED, AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
    }

    private fun renderState(state: PlayerState) {
        screenStateLiveData.postValue(state)
    }

    companion object {
        const val TIMER_DELAY = 300L
    }
}