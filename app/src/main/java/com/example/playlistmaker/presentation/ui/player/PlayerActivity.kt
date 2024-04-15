package com.example.playlistmaker.presentation.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.mediaPlayer.AndroidMediaPlayerHandler
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {
    private var mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private var handler = Handler(Looper.getMainLooper())
    private var timerRunnable = object:Runnable {
        override fun run() {
            if(mediaPlayerInteractor.getPlayerState() == AndroidMediaPlayerHandler.PLAYER_STATE_PLAYING){
                trackDurationTV.text = mediaPlayerInteractor.getCurrentPosFormatted()
                handler.postDelayed(this, TIMER_DELAY)
            }
        }

    }
    private lateinit var mediaControlButton: ImageButton
    private lateinit var trackDurationTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val track = Gson().fromJson(intent.getStringExtra(TRACK_PLAYER_KEY), Track::class.java)
        val trackIV = findViewById<ImageView>(R.id.track_image_iv)
        val trackTitleTV = findViewById<TextView>(R.id.track_title_tv)
        val trackAuthorTV = findViewById<TextView>(R.id.track_author_tv)
        trackDurationTV = findViewById<TextView>(R.id.track_duration_tv)
        mediaControlButton = findViewById<ImageButton>(R.id.play_btn)
        val durationInfoTV = findViewById<TextView>(R.id.duration_info_tv)
        val albumInfoTV = findViewById<TextView>(R.id.album_info_tv)
        val albumHeaderTV = findViewById<TextView>(R.id.album_header_tv)
        val yearInfoTV = findViewById<TextView>(R.id.year_info_tv)
        val genreInfoTV = findViewById<TextView>(R.id.genre_info_tv)
        val countryInfoTV = findViewById<TextView>(R.id.country_info_tv)
        val backButton = findViewById<ImageButton>(R.id.back_btn)
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_image_rounding)))
            .into(trackIV)
        trackTitleTV.text = track.trackName
        trackAuthorTV.text = track.artistName
        durationInfoTV.text = track.trackTimeMillis
        preparePLayer(track.previewUrl)

        mediaControlButton.setOnClickListener {
            playbackControl()
        }
        if(track.collectionName.isEmpty()){
            albumInfoTV.visibility = View.GONE
            albumHeaderTV.visibility = View.GONE
        }
        else{
            albumInfoTV.text = track.collectionName
        }
        yearInfoTV.text = track.releaseDate.substring(0, track.releaseDate.indexOf('-'))
        genreInfoTV.text = track.primaryGenreName
        countryInfoTV.text = track.country
        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
        mediaPlayerInteractor.releasePlayer()
    }
    private fun playbackControl(){
        when(mediaPlayerInteractor.getPlayerState()){
            AndroidMediaPlayerHandler.PLAYER_STATE_PLAYING -> {
                pausePlayer()
            }
            AndroidMediaPlayerHandler.PLAYER_STATE_PREPARED, AndroidMediaPlayerHandler.PLAYER_STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun preparePLayer(url: String){
        mediaPlayerInteractor.preparePlayer(url)
        mediaPlayerInteractor.setCompletionListener {
            handler.removeCallbacks(timerRunnable)
            mediaControlButton.setImageResource(R.drawable.play_button)
            trackDurationTV.text = resources.getText(R.string.track_duration_placeholder)
        }

    }
    private fun startPlayer(){
        mediaPlayerInteractor.play()
        mediaControlButton.setImageResource(R.drawable.pause_button)
        handler.post(timerRunnable)
    }
    private fun pausePlayer(){
        mediaPlayerInteractor.pause()
        mediaControlButton.setImageResource(R.drawable.play_button)
        handler.removeCallbacks(timerRunnable)
    }
    companion object{
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
        const val TIMER_DELAY = 300L
    }
}