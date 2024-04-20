package com.example.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.App
class ThemeSwitcherImpl(private val application: Application): ThemeSwitcher {
    private val sharedPrefs = application.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES,
        MODE_PRIVATE
    )
    override fun switchTheme(dark: Boolean) {
        (application as App).switchTheme(dark)
        if (dark){
            sharedPrefs
                .edit()
                .putString(THEME_MODE_KEY, DARK_MODE_VALUE)
                .apply()
        }
        else{
            sharedPrefs
                .edit()
                .putString(THEME_MODE_KEY, LIGHT_MODE_VALUE)
                .apply()
        }
    }

    override fun getSavedTheme() = (application as App).darkTheme

    companion object {

        const val THEME_MODE_KEY = "key_for_theme_mode"
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARK_MODE_VALUE = "dark"
        const val LIGHT_MODE_VALUE = "light"
    }

}