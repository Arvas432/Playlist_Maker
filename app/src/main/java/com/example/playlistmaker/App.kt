package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App: Application() {

    var darkTheme = false
    override fun onCreate() {
        Creator.setApplication(this)
        val savedThemeValue = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE).getString(
            THEME_MODE_KEY, "")
        if(savedThemeValue.isNullOrEmpty()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        else{
            when(savedThemeValue){
                "dark" -> {
                    darkTheme = true
                }
                "light" -> {
                    darkTheme = false
                }
            }
            switchTheme(darkTheme)
        }
        super.onCreate()
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {

        const val THEME_MODE_KEY = "key_for_theme_mode"
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    }
}