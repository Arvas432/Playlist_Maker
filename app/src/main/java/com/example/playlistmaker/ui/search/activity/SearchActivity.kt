package com.example.playlistmaker.ui.search.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.SearchState
import com.example.playlistmaker.ui.search.adapters.TrackListAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var binding :ActivitySearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private var searchFieldEmpty: Boolean = true
    private var tracks = ArrayList<Track>()
    private lateinit var trackListAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]
        viewModel.getScreenStateLiveData().observe(this){
            renderState(it)
        }
        binding.searchFieldEdittext.setText(viewModel.getSearchData())
        trackListAdapter = TrackListAdapter(tracks, viewModel)
        binding.trackListRecyclerview.adapter = trackListAdapter
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            tracks.clear()
            trackListAdapter.notifyDataSetChanged()
            setDefaultScreenState()
        }
        val searchFieldTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    if(binding.searchFieldEdittext.hasFocus() && binding.searchFieldEdittext.text.isEmpty()){
                        viewModel.showHistory()
                    }
                    else{
                        setDefaultScreenState()
                    }
                    binding.clearButton.isVisible = false
                }
                else{
                    setDefaultScreenState()
                    searchFieldEmpty = false
                    binding.clearButton.isVisible = true
                    viewModel.setSearchData(s.toString())
                    viewModel.searchDebounce()
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
        binding.searchFieldEdittext.addTextChangedListener(searchFieldTextWatcher)
        binding.searchFieldEdittext.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus &&   binding.searchFieldEdittext.text.isEmpty()){
                viewModel.showHistory()
            }
        }
        binding.clearButton.setOnClickListener {
            tracks.clear()
            trackListAdapter.notifyDataSetChanged()
            currentFocus?.let {
                val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
            binding.searchFieldEdittext.setText("")
        }

        binding.searchFieldEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
              viewModel.immediateSearch()
            }
            false
        }
        binding.refreshButton.setOnClickListener {
            viewModel.searchLast()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

    }
    private fun setNetworkErrorScreenState(){
        setDefaultScreenState()
        binding.refreshButton.isVisible = true
        binding.errorPlaceholderLayout.isVisible = true
        binding.errorText.isVisible = true
        binding.errorPlaceholderImage.isVisible = true
        binding.errorPlaceholderImage.setImageResource(R.drawable.no_connection_placeholder)
        binding.errorText.text = resources.getText(R.string.connection_problem)
    }
    private fun setLoadingScreenState(){
        setDefaultScreenState()
        binding.searchPb.isVisible = true
    }
    private fun setEmptyResultsScreenState(){
        setDefaultScreenState()
        binding.refreshButton.isVisible = true
        binding.errorPlaceholderLayout.isVisible = true
        binding.errorText.isVisible = true
        binding.errorPlaceholderImage.isVisible = true
        binding.errorPlaceholderImage.setImageResource(R.drawable.no_search_results_placeholder)
        binding.errorText.text = resources.getText(R.string.no_search_results)
    }
    private fun setDefaultScreenState(){
        binding.searchPb.isVisible = false
        binding.clearHistoryButton.isVisible = false
        binding.trackListRecyclerview.isVisible = false
        binding.searchHistoryTextview.isVisible = false
        binding.errorPlaceholderLayout.isVisible = false
        binding.errorPlaceholderLayout.isVisible = false
        binding.refreshButton.isVisible = false
        binding.errorText.isVisible = false
    }
    private fun setSearchHistoryScreenState(searchHistory: List<Track>){
        setDefaultScreenState()
        tracks.clear()
        tracks.addAll(searchHistory)
        trackListAdapter.notifyDataSetChanged()
        binding.trackListRecyclerview.isVisible = true
        binding.clearHistoryButton.isVisible = true
        binding.searchHistoryTextview.isVisible = true
    }
    private fun setContentScreenState(results: List<Track>){
        setDefaultScreenState()
        tracks.clear()
        tracks.addAll(results)
        trackListAdapter.notifyDataSetChanged()
        binding.trackListRecyclerview.isVisible = true
    }
    private fun renderState(state: SearchState){
        when(state){
            is SearchState.Default ->{setDefaultScreenState()}
            is SearchState.Loading ->{setLoadingScreenState()}
            is SearchState.NetworkError ->{setNetworkErrorScreenState()}
            is SearchState.EmptyResults ->{setEmptyResultsScreenState()}
            is SearchState.SearchHistory ->{setSearchHistoryScreenState(state.tracks)}
            is SearchState.Content ->{setContentScreenState(state.tracks)}
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}