package com.example.playlistmaker.di

import android.content.ContentResolver
import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.playlists.local.PlaylistImageStorageHandler
import com.example.playlistmaker.data.playlists.local.PlaylistImageStorageHandlerImpl
import com.example.playlistmaker.data.search.LocalTrackStorageHandler
import com.example.playlistmaker.data.search.impl.SharedPreferencesLocalTrackStorageHandler
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.impl.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.ThemeSwitcher
import com.example.playlistmaker.data.settings.impl.ThemeSwitcherImpl
import com.example.playlistmaker.utils.TrackMapper
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module{
    val iTunesBaseUrl = "https://itunes.apple.com"
    val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    single<ITunesApi>{
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }
    single<ContentResolver> {
        androidContext().contentResolver
    }
    single<PlaylistImageStorageHandler>{
        PlaylistImageStorageHandlerImpl(get(), androidContext())
    }
    single{
        androidContext().getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }
    single{
        Gson()
    }
    single<LocalTrackStorageHandler>{
        SharedPreferencesLocalTrackStorageHandler(get(), get())
    }
    single<NetworkClient>{
        RetrofitNetworkClient(get())
    }
    single<ThemeSwitcher>{
        ThemeSwitcherImpl(get(), get())
    }
    factory{
        MediaPlayer()
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single{
        TrackMapper()
    }
}
