package com.example.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.PlayerFavoriteStatusState
import com.example.playlistmaker.ui.player.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel<PlayerViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = Gson().fromJson(intent.getStringExtra(TRACK_PLAYER_KEY), Track::class.java)
        Log.i("Player", track.trackName)
        viewModel.getScreenStateLiveData().observe(this){
            renderState(it)
            Log.i("STATE", it.toString())
        }
        viewModel.getFavoriteStateLiveData().observe(this){
            if(it is PlayerFavoriteStatusState.FavoriteState){
                if(it.isFavorite){
                    binding.favoritesBtn.setImageResource(R.drawable.add_to_favorites_button_pressed)
                }else{
                    binding.favoritesBtn.setImageResource(R.drawable.add_to_favorites_button_unpressed)
                }
            }
        }
        viewModel.getCurrentPositionLiveData().observe(this){
            binding.trackDurationTv.text = it
        }
        viewModel.preparePlayer(track)
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
        binding.backBtn.setOnClickListener {
            finish()
        }
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


    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()

    }
    override fun onDestroy() {
        if (!isChangingConfigurations) {
            viewModel.releasePlayer()
        } else{
            viewModel.pausePlayer()
            viewModel.beforeScreenRotate()
        }
        super.onDestroy()
    }
    companion object{
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
    }
}