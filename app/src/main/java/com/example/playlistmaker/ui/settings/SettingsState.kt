package com.example.playlistmaker.ui.settings

sealed class SettingsState{
    object switchOn: SettingsState()
    object switchOff: SettingsState()
}
