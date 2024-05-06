package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.ui.settings.SettingsState

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor) :ViewModel(){
    private var screenStateLiveData = MutableLiveData<SettingsState>()
    fun getScreenStateLiveData(): LiveData<SettingsState> = screenStateLiveData
    init {
        val themeSettings = settingsInteractor.getThemeSettings()
        when(themeSettings.darkTheme){
            true->{renderState(SettingsState.switchOn)}
            false->{renderState(SettingsState.switchOff)}
        }
    }
    fun switchTheme(darkMode: Boolean){
        settingsInteractor.updateThemeSetting(ThemeSettings(darkMode))
        if(darkMode){
            screenStateLiveData.postValue(SettingsState.switchOn)
        }else{
            screenStateLiveData.postValue(SettingsState.switchOff)
        }
    }
    private fun renderState(state: SettingsState){
        screenStateLiveData.postValue(state)
    }

}