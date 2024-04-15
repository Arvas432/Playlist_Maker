package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.SharedPreferencesLocalTrackStorageHandler
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.player.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeSwitcherImpl
import com.example.playlistmaker.data.search.network.impl.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl

object Creator {
    private lateinit var application: Application
    const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    fun setApplication(application: Application){
        this.application = application
    }
    private fun getSettingsRepository(): SettingsRepository{
        return SettingsRepositoryImpl(ThemeSwitcherImpl(this.application))
    }
    fun provideSettingsInteractor(): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository{
        return SearchHistoryRepositoryImpl(
            SharedPreferencesLocalTrackStorageHandler(application.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
                Context.MODE_PRIVATE
            ))
        )
    }
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    private fun getMediaPlayerHandler(): MediaPlayerRepository {
        return AndroidMediaPlayerRepositoryImpl()
    }
    fun provideMediaPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getMediaPlayerHandler())
    }
}