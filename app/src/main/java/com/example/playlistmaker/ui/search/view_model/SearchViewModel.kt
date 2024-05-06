package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.models.SearchResultType
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TracksSearchResult
import com.example.playlistmaker.ui.search.SearchState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel(){
    private var screenStateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    private var searchData: String = SEARCH_DEF
    private var lastSearch: String = SEARCH_DEF
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        iTunesSearch(searchData)
    }
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable, SEARCH_RUNNABLE_TOKEN)
        handler.postDelayed(searchRunnable ,
            SEARCH_RUNNABLE_TOKEN,
            SEARCH_DEBOUNCE_DELAY
        )
    }
    fun setSearchData(input: String){
        searchData = input
    }
    fun getSearchData() = searchData
    fun getScreenStateLiveData(): LiveData<SearchState> = screenStateLiveData

    fun clearHistory(){
        searchHistoryInteractor.clear()
        renderState(SearchState.Default)
    }
    fun showHistory(){
        handler.removeCallbacksAndMessages(null)
        val history = searchHistoryInteractor.read()
        if(history.isNotEmpty()){
            renderState(SearchState.SearchHistory(history))
        }else{
            renderState(SearchState.Default)
        }
    }
    fun writeToHistory(input: Track){
        searchHistoryInteractor.write(input)
    }
    fun immediateSearch(){
        handler.removeCallbacks(searchRunnable, SEARCH_RUNNABLE_TOKEN)
        iTunesSearch(searchData)
    }
    fun searchLast(){
        handler.removeCallbacks(searchRunnable, SEARCH_RUNNABLE_TOKEN)
        iTunesSearch(lastSearch)
    }
    private fun iTunesSearch(query: String) {
        tracksInteractor.searchTracks(query, object: TracksInteractor.TracksConsumer {
            override fun consume(searchResult: TracksSearchResult) {
                handler.post {
                    when(searchResult.type){
                        SearchResultType.SUCCESS ->{
                            renderState(SearchState.Content(searchResult.tracks))
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
        )
    }

    private fun renderState(state: SearchState){
        screenStateLiveData.postValue(state)
    }
    fun onDestroy(){
        handler.removeCallbacksAndMessages(null)
    }
    companion object {
        const val SEARCH_RUNNABLE_TOKEN = 1
        const val SEARCH_DEF = ""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}