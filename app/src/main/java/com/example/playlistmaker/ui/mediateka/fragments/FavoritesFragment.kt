package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.mediateka.states.FavoritesState
import com.example.playlistmaker.ui.mediateka.view_model.FavoritesFragmentViewModel
import com.example.playlistmaker.ui.search.adapters.TrackListAdapter
import com.example.playlistmaker.ui.search.fragments.SearchFragment
import com.example.playlistmaker.utils.BindingFragment
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {
    private val favoritesViewModel: FavoritesFragmentViewModel by viewModel<FavoritesFragmentViewModel>()
    private var tracks = ArrayList<Track>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var trackListAdapter: TrackListAdapter
    private var clickAllowed = true
    private fun clickDebounce(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                clickAllowed = true
            }
        }
        return current
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel.getScreenStateLiveData().observe(viewLifecycleOwner){
            renderState(it)
        }
        favoritesViewModel.getFavorites()
        onTrackClickDebounce = { track ->
            run {
                if (clickDebounce()) {
                    findNavController().navigate(
                        R.id.action_mediatekaFragment_to_playerFragment,
                        bundleOf(SearchFragment.TRACK_PLAYER_KEY to Gson().toJson(track))
                    )
                }
            }
        }
        trackListAdapter = TrackListAdapter(tracks) { track ->
            onTrackClickDebounce(track)
        }
        binding.trackListRecyclerview.adapter = trackListAdapter
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.getFavorites()
    }
    override fun onPause() {
        super.onPause()
        clickAllowed = true
    }

    private fun renderState(state: FavoritesState){
        when(state){
            is FavoritesState.Default -> Unit
            is FavoritesState.Content -> renderContent(state.tracks)
            is FavoritesState.Empty -> renderEmpty()
        }
    }
    private fun renderContent(favorites: List<Track>){
        binding.errorPlaceholderImage.isVisible = false
        binding.errorText.isVisible = false
        binding.trackListRecyclerview.isVisible = true
        tracks.clear()
        tracks.addAll(favorites)
        trackListAdapter.notifyDataSetChanged()
    }
    private fun renderEmpty(){
        binding.errorPlaceholderImage.isVisible = true
        binding.errorText.isVisible = true
        binding.trackListRecyclerview.isVisible = false
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        @JvmStatic
        fun newInstance() = FavoritesFragment().apply {  }
    }
}