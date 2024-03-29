package com.example.playlistmaker

import com.example.playlistmaker.data.MediaPlayerHandler
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.mediaPlayer.AndroidMediaPlayerHandler
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    private fun getMediaPlayerHandler(): MediaPlayerHandler{
        return AndroidMediaPlayerHandler()
    }
    fun provideMediaPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getMediaPlayerHandler())
    }
}