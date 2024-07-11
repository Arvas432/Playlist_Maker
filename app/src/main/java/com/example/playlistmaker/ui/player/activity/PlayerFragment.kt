package com.example.playlistmaker.ui.player.activity

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.adapters.PlaylistSmallAdapter
import com.example.playlistmaker.ui.player.states.PlayerFavoriteStatusState
import com.example.playlistmaker.ui.player.states.PlayerState
import com.example.playlistmaker.ui.player.states.PlaylistSheetState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.rootActivity.RootActivity
import com.example.playlistmaker.ui.search.fragments.SearchFragment.Companion.TRACK_PLAYER_KEY
import com.example.playlistmaker.utils.BindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment: BindingFragment<FragmentPlayerBinding>() {
    private val viewModel: PlayerViewModel by viewModel<PlayerViewModel>()
    private lateinit var track: Track
    private var changingConfiguration = false
    private lateinit var playlistsAdapter: PlaylistSmallAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var playlists = mutableListOf<Playlist>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = Gson().fromJson(requireArguments().getString(TRACK_PLAYER_KEY), Track::class.java)
        if (savedInstanceState==null){
           viewModel.preparePlayer(track)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changingConfiguration = false
        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner){
            renderState(it)
            Log.i("STATE", it.toString())
        }
        viewModel.getFavoriteStateLiveData().observe(viewLifecycleOwner){
            if(it is PlayerFavoriteStatusState.FavoriteState){
                if(it.isFavorite){
                    binding.favoritesBtn.setImageResource(R.drawable.add_to_favorites_button_pressed)
                }else{
                    binding.favoritesBtn.setImageResource(R.drawable.add_to_favorites_button_unpressed)
                }
            }
        }
        viewModel.getCurrentPositionLiveData().observe(viewLifecycleOwner){
            binding.trackDurationTv.text = it
        }
        viewModel.getPlaylistSheetLiveData().observe(viewLifecycleOwner){
            renderBottomSheetState(it)
        }
        playlistsAdapter = PlaylistSmallAdapter(playlists){ playlist, position ->
            viewModel.addTrackToPlaylist(playlist,track)
            playlistsAdapter.notifyItemChanged(position)
        }
        binding.playlistsRv.adapter = playlistsAdapter
        viewModel.requestFavoriteStatus()
        viewModel.requestPlayerStatusUpdate()
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_image_rounding)))
            .into(binding.trackImageIv)
        binding.trackTitleTv.text = track.trackName
        binding.trackAuthorTv.text = track.artistName
        binding.durationInfoTv.text = track.formattedDuration
        binding.playBtn.setOnClickListener {
            Log.i("BUTTON", "play button clicked")
            viewModel.playbackControl()
        }
        if(track.collectionName.isEmpty()){
            binding.albumInfoTv.isVisible = false
            binding.albumHeaderTv.isVisible = false

        }
        else{
            binding.albumInfoTv.text = track.collectionName
        }
        binding.yearInfoTv.text = track.releaseDate.substring(0, track.releaseDate.indexOf('-'))
        binding.genreInfoTv.text = track.primaryGenreName
        binding.countryInfoTv.text = track.country
        binding.favoritesBtn.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
        binding.addToPlaylistBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.requestPlaylists()
        }
        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistCreationFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun renderPlaylistContent(content: List<Playlist>){
        playlists.clear()
        playlists.addAll(content)
        playlistsAdapter.notifyDataSetChanged()
    }
    private fun renderTrackSuccessfullyAdded(name: String){
        (requireActivity() as RootActivity).showFakeToast(
            getString(
                R.string.added_to_playlist,
                name
            ))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }
    private fun renderTrackAlreadyAdded(name: String){
        (requireActivity() as RootActivity).showFakeToast(
            getString(
                R.string.track_already_added_to_playlist,
                name
            ))
    }
    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylists()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
        viewModel.beforeScreenRotate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(screenRotateTag, true)
        super.onSaveInstanceState(outState)
    }
    override fun onDestroy() {
        if (changingConfiguration) {
            viewModel.pausePlayer()
            viewModel.beforeScreenRotate()
        }
        super.onDestroy()
    }
    private fun renderState(state: PlayerState){
        when(state){
            is PlayerState.Default -> Unit
            is PlayerState.Prepared ->{
                binding.playBtn.setImageResource(R.drawable.play_button)
                binding.trackDurationTv.text = resources.getText(R.string.track_duration_placeholder)
            }
            is PlayerState.Playing ->{
                binding.playBtn.setImageResource(R.drawable.pause_button)
            }
            is PlayerState.Paused ->{
                binding.playBtn.setImageResource(R.drawable.play_button)
            }

        }
    }
    private fun renderBottomSheetState(state: PlaylistSheetState){
        when(state){
            is PlaylistSheetState.Default -> Unit
            is PlaylistSheetState.Content -> renderPlaylistContent(state.playlists)
            is PlaylistSheetState.TrackAdded -> renderTrackSuccessfullyAdded(state.playlistName)
            is PlaylistSheetState.TrackAlreadyAdded -> renderTrackAlreadyAdded(state.playlistName)
        }
    }
    companion object{
        const val screenRotateTag = "SCREEN_ROTATE"
    }
}