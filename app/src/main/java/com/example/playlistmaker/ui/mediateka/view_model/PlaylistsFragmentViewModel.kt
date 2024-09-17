package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.ui.mediateka.states.PlaylistsState
import kotlinx.coroutines.launch


class PlaylistsFragmentViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {
    private var screenStateLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Default)
    fun getScreenStateLiveData(): LiveData<PlaylistsState> = screenStateLiveData
    fun loadPlaylists(){
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect{
                if (it.isEmpty()){
                    screenStateLiveData.postValue(PlaylistsState.Empty)
                }else{
                    screenStateLiveData.postValue(PlaylistsState.Content(it))
                }
            }
        }
    }
}