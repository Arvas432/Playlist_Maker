package com.example.playlistmaker.ui.search.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.ui.search.states.SearchState
import com.example.playlistmaker.ui.search.adapters.TrackListAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var searchFieldEmpty: Boolean = true
    private var tracks = ArrayList<Track>()
    private lateinit var trackListAdapter: TrackListAdapter
    private var clickAllowed = true
    private fun clickDebounce(): Boolean{
        val current = clickAllowed
        if(clickAllowed){
            clickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                clickAllowed = true
            }
        }
        return current
    }
    //хихи
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner){
            renderState(it)
        }
        binding.searchFieldEdittext.setText(viewModel.getSearchData())


        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            tracks.clear()
            trackListAdapter.notifyDataSetChanged()
            setDefaultScreenState()
        }
        onTrackClickDebounce = {
            track ->
            run {
                if (clickDebounce()){
                    viewModel.writeToHistory(track)
                    findNavController().navigate(
                        R.id.action_searchFragment_to_playerActivity,
                        bundleOf(TRACK_PLAYER_KEY to Gson().toJson(track))
                    )
                }
            }
        }
        trackListAdapter = TrackListAdapter(tracks) { track ->
            onTrackClickDebounce(track)
        }
        binding.trackListRecyclerview.adapter = trackListAdapter
        val searchFieldTextWatcher = object : TextWatcher {
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
            requireActivity().currentFocus?.let {
                val inputMethodManager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)!!
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
    companion object{
        @JvmStatic
        fun newInstance() = SearchFragment.apply {  }
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}