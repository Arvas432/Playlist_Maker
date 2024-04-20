package com.example.playlistmaker.data.settings.impl

interface ThemeSwitcher {
    fun switchTheme(dark: Boolean)
    fun getSavedTheme(): Boolean
}