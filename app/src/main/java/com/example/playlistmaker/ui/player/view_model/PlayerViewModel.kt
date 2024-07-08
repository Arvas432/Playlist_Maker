package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.player.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.PlayerFavoriteStatusState
import com.example.playlistmaker.ui.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    private var currentPositionLiveData = MutableLiveData<String>()
    private var favoriteStateLiveData = MutableLiveData<PlayerFavoriteStatusState>(PlayerFavoriteStatusState.Default)
    private var timerJob: Job? = null
    private lateinit var currentTrack: Track
    private var changingScreenOrientation = false

    fun getScreenStateLiveData(): LiveData<PlayerState> = screenStateLiveData
    fun getFavoriteStateLiveData(): LiveData<PlayerFavoriteStatusState> = favoriteStateLiveData
    fun getCurrentPositionLiveData(): LiveData<String> = currentPositionLiveData
    fun preparePlayer(track: Track) {
        Log.i("PREPARING PLAYER", changingScreenOrientation.toString())
        if (!changingScreenOrientation) {
            currentTrack = track
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
        changingScreenOrientation = false
    }

    private fun startPlayer() {
        Log.i("VIEWMODEL", "START PLAYER CALLED")
        playerInteractor.play()
        renderState(PlayerState.Playing)
        timerJob = viewModelScope.launch {
            while (playerInteractor.getPlayerState() == AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PLAYING) {
                delay(TIMER_DELAY)
                currentPositionLiveData.postValue(playerInteractor.getCurrentPosFormatted())
            }
        }
    }
    fun requestPlayerStatusUpdate(){
        when (playerInteractor.getPlayerState()) {
            AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PAUSED -> renderState(PlayerState.Paused)
            AndroidMediaPlayerRepositoryImpl.PLAYER_STATE_PLAYING -> renderState(PlayerState.Playing)
            else -> Unit
        }
    }
    fun onFavoriteClicked() {
        if (currentTrack.isFavorite) {
            viewModelScope.launch {
                favoritesInteractor.removeFromFavorites(currentTrack)
            }
        } else {
            viewModelScope.launch {
                favoritesInteractor.addToFavorites(currentTrack)
            }
        }
        currentTrack.isFavorite = !currentTrack.isFavorite
        favoriteStateLiveData.postValue(PlayerFavoriteStatusState.FavoriteState(currentTrack.isFavorite))
    }

    fun beforeScreenRotate() {
        changingScreenOrientation = true
    }
    fun requestFavoriteStatus(){
        favoriteStateLiveData.postValue(PlayerFavoriteStatusState.FavoriteState(currentTrack.isFavorite))
    }

    fun pausePlayer() {
        playerInteractor.pause()
        renderState(PlayerState.Paused)
        timerJob?.cancel()
    }

    fun playbackControl() {
        Log.i("VIEWMODEL", "PLAYBACK CONTROLL CALLED")
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
        Log.i("VIEWMODEL", "rendering state ${state.toString()}")
        screenStateLiveData.postValue(state)
    }

    companion object {
        const val TIMER_DELAY = 300L
    }
}