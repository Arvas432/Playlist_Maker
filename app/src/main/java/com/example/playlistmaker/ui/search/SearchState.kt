package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.search.models.Track

sealed class SearchState{
    object Default: SearchState()
    object Loading: SearchState()
    object NetworkError: SearchState()
    object EmptyResults: SearchState()
    data class SearchHistory(val tracks: List<Track>) : SearchState()
    data class Content(val tracks: List<Track>): SearchState()
}
