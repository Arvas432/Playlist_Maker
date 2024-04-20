package com.example.playlistmaker.di
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }
    single<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }
    single<TracksInteractor>{
        TracksInteractorImpl(get())
    }
    single<PlayerInteractor>{
        PlayerInteractorImpl(get())
    }
}