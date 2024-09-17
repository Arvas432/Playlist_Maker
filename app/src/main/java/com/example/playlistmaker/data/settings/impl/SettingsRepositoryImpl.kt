package com.example.playlistmaker.data.settings.impl

import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(private val themeSwitcher: ThemeSwitcher): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(themeSwitcher.getSavedTheme())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        themeSwitcher.switchTheme(settings.darkTheme)
    }
}