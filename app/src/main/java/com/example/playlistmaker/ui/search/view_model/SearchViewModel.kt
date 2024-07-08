package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.models.SearchResultType
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.states.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel(){
    private var screenStateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    private var searchData: String = SEARCH_DEF
    private var lastSearch: String = SEARCH_DEF
    private var currentSearchHistory = mutableListOf<Track>()
    private var searchJob: Job? = null
    fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            iTunesSearch(searchData)
        }
    }
    fun setSearchData(input: String){
        searchData = input
    }
    fun getSearchData() = searchData
    fun getScreenStateLiveData(): LiveData<SearchState> = screenStateLiveData

    fun clearHistory(){
        currentSearchHistory.clear()
        searchHistoryInteractor.clear()
        renderState(SearchState.Default)
    }
    fun showHistory(){
        searchJob?.cancel()
        viewModelScope.launch {
            searchHistoryInteractor.read().collect { history ->
                if(history.isNotEmpty()){
                    currentSearchHistory.clear()
                    currentSearchHistory.addAll(history)
                    renderState(SearchState.SearchHistory(currentSearchHistory))
                }else{
                    currentSearchHistory.clear()
                    renderState(SearchState.Default)
                }
            }
        }

    }
    fun writeToHistory(input: Track){
        searchHistoryInteractor.write(input)
    }
    fun immediateSearch(){
        searchJob?.cancel()
        iTunesSearch(searchData)
    }
    fun searchLast(){
        searchJob?.cancel()
        iTunesSearch(lastSearch)
    }
    private fun iTunesSearch(query: String) {
        if (query.isNotEmpty()){
            renderState(SearchState.Loading)
            viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect{
                        when(it.type){
                            SearchResultType.SUCCESS ->{
                                renderState(SearchState.Content(it.tracks))
                            }
                            SearchResultType.EMPTY -> {
                                renderState(SearchState.EmptyResults)
                            }
                            SearchResultType.LOADING -> {
                                renderState(SearchState.Loading)
                            }
                            SearchResultType.ERROR -> {
                                renderState(SearchState.NetworkError)
                                lastSearch = query
                            }
                        }
                    }
            }
        }
    }

    private fun renderState(state: SearchState){
        screenStateLiveData.postValue(state)
    }
    companion object {
        const val SEARCH_DEF = ""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}