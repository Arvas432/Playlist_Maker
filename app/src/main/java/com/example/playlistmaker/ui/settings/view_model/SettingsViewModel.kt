package com.example.playlistmaker.ui.settings.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.ui.settings.states.SettingsState

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor) :ViewModel(){
    private var screenStateLiveData = MutableLiveData<SettingsState>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private fun switchDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            renderState(SettingsState.switchInactive)
            handler.postDelayed({
                isClickAllowed = true
                renderState(SettingsState.switchActive)}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    fun getScreenStateLiveData(): LiveData<SettingsState> = screenStateLiveData
    init {
        val themeSettings = settingsInteractor.getThemeSettings()
        when(themeSettings.darkTheme){
            true->{renderState(SettingsState.switchOn)}
            false->{renderState(SettingsState.switchOff)}
        }
    }
    fun switchTheme(darkMode: Boolean){
        if(switchDebounce()){
            settingsInteractor.updateThemeSetting(ThemeSettings(darkMode))
            if(darkMode){
                screenStateLiveData.postValue(SettingsState.switchOn)
            }else{
                screenStateLiveData.postValue(SettingsState.switchOff)
            }
        }

    }
    private fun renderState(state: SettingsState){
        screenStateLiveData.postValue(state)
    }
    companion object{
        const val CLICK_DEBOUNCE_DELAY = 100L
    }

}