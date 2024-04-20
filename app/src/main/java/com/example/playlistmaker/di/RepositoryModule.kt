package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module{
    single<SettingsRepository>{
        SettingsRepositoryImpl(get())
    }
    single<SearchHistoryRepository>{
        SearchHistoryRepositoryImpl(get())
    }
    single<TracksRepository>{
        TracksRepositoryImpl(get())
    }
    factory<MediaPlayerRepository>{
        AndroidMediaPlayerRepositoryImpl(get())
    }
}