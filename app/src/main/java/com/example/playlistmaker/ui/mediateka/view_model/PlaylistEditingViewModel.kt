package com.example.playlistmaker.ui.mediateka.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import kotlinx.coroutines.launch

class PlaylistEditingViewModel(interactor: PlaylistsInteractor) :
    PlaylistCreationViewModel(interactor) {
    private var screenStateLiveData = MutableLiveData<Playlist>()
    private lateinit var playlist: Playlist
    fun getPlaylistLiveData(): LiveData<Playlist> = screenStateLiveData
    fun requestPlaylistInfo(playlistId: Long) {
        viewModelScope.launch {
            interactor.getPlaylistById(playlistId).collect {
                screenStateLiveData.postValue(it)
                playlist = it
            }
        }
    }

    override fun setName(name: String) {
        playlist = playlist.copy(name = name)
    }

    override fun setDescription(description: String) {
        playlist = playlist.copy(description = description)
    }

    override fun setUri(uri: Uri) {
        playlist = playlist.copy(imageFileUri = uri)
    }

    override fun finalizePlaylistAction() {
        viewModelScope.launch {
            interactor.updatePlaylist(playlist)
        }
    }
}