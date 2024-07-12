package com.example.playlistmaker.ui.mediateka.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import kotlinx.coroutines.launch

class PlaylistCreationViewModel(private var interactor: PlaylistsInteractor):ViewModel() {
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistImageUri: Uri? = null
    fun setName(name: String){
        playlistName = name
    }
    fun setDescription(description: String){
        playlistDescription = description
    }
    fun setUri(uri: Uri){
        playlistImageUri = uri
    }
    fun createPlaylist(){
        viewModelScope.launch {
            interactor.addPlaylist(Playlist(playlistName,playlistDescription,playlistImageUri, mutableListOf(),0))
            interactor.getAllPlaylists().collect{
                Log.i("PLAYLIST", it.toString())
            }
        }
    }
    fun requestUri(): Uri?{
        return playlistImageUri
    }
    fun checkUnsavedChanges(): Boolean{
        return playlistName.isNotBlank() || playlistDescription.isNotBlank() || playlistImageUri!=null
    }

}