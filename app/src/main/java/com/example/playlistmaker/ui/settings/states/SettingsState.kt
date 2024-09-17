package com.example.playlistmaker.ui.settings.states

sealed class SettingsState{
    object switchOn: SettingsState()
    object switchOff: SettingsState()
    object switchActive: SettingsState()
    object switchInactive: SettingsState()
}
