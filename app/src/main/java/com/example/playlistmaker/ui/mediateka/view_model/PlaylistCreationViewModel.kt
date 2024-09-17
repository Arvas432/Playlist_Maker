package com.example.playlistmaker.ui.mediateka.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import kotlinx.coroutines.launch

open class PlaylistCreationViewModel(protected var interactor: PlaylistsInteractor):ViewModel() {
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistImageUri: Uri? = null
    open fun setName(name: String){
        playlistName = name
    }
    open fun setDescription(description: String){
        playlistDescription = description
    }
    open fun setUri(uri: Uri){
        playlistImageUri = uri
    }
    open fun finalizePlaylistAction(){
        viewModelScope.launch {
            interactor.addPlaylist(Playlist(playlistName,playlistDescription,playlistImageUri, mutableListOf(),0))
        }
    }
    fun requestUri(): Uri?{
        return playlistImageUri
    }
    fun checkUnsavedChanges(): Boolean{
        return playlistName.isNotBlank() || playlistDescription.isNotBlank() || playlistImageUri!=null
    }

}