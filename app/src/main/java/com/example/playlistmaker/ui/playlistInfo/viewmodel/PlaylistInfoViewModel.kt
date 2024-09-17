package com.example.playlistmaker.ui.playlistInfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.playlistInfo.states.PlaylistInfoStates
import com.example.playlistmaker.utils.TrackCountFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(private val playlistsInteractor: PlaylistsInteractor,
    private val trackCountFormatter: TrackCountFormatter) : ViewModel() {
    private var screenStateLiveData =
        MutableLiveData<PlaylistInfoStates>(PlaylistInfoStates.Default)
    private var shareTextLiveData = MutableLiveData<String>()
    private var playlistDeletionLiveData = MutableLiveData<Boolean>(false)
    private val dateFormat by lazy { SimpleDateFormat("m", Locale.getDefault()) }
    private val trackDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun getScreenStateLiveData(): LiveData<PlaylistInfoStates> = screenStateLiveData
    fun getShareTextLiveData(): LiveData<String> = shareTextLiveData
    fun getPlaylistDeletionLiveData(): LiveData<Boolean> = playlistDeletionLiveData
    fun requestPlaylistInfo(playlistId: Long) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(playlistId).collect { playlist ->
                playlistsInteractor.getTracksByPlaylistId(playlistId).collect { tracks ->
                    var tracksLengthSum = 0L
                    if (tracks.isEmpty()){
                        renderState(PlaylistInfoStates.EmptyPlaylist(playlist))
                    } else{
                        tracks.forEach { tracksLengthSum += it.trackTimeMillis ?: 0 }
                        renderState(PlaylistInfoStates.PlaylistContent(
                            playlist,
                            tracks,
                            dateFormat.format(tracksLengthSum).toInt()
                        ))
                        requestPlaylistShareText(playlist,tracks)
                    }
                }
            }
        }

    }
    fun requestPlaylistShareText(playlist: Playlist, tracks: List<Track>){
        var message = ""
        message+= playlist.name
        if (playlist.description.isNotEmpty()) {
            message += "\n${playlist.description}"
        }
        message+="\n${trackCountFormatter.formatTrackCount(playlist.trackCount)}"
        tracks.forEach { track ->  message+="\n${tracks.indexOf(track)+1}. ${track.artistName} - ${track.trackName} (${trackDateFormat.format(track.trackTimeMillis)})"}
        Log.i("Share message", message)
        shareTextLiveData.postValue(message)
    }
    fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int){
        viewModelScope.launch {
            playlistsInteractor.removeTrackFromPlaylist(playlistId, trackId)
            requestPlaylistInfo(playlistId)
        }
    }
    fun deletePlaylist(playlistId: Long){
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlistId)
            playlistDeletionLiveData.postValue(true)
        }
    }

    private fun renderState(state: PlaylistInfoStates) {
        screenStateLiveData.postValue(state)
    }
}