package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        val releaseDateFormat by lazy {SimpleDateFormat("yyyy", Locale.getDefault())}
        val track = Gson().fromJson(intent.getStringExtra(TRACK_PLAYER_KEY), Track::class.java)
        val trackIV = findViewById<ImageView>(R.id.track_image_iv)
        val trackTitleTV = findViewById<TextView>(R.id.track_title_tv)
        val trackAuthorTV = findViewById<TextView>(R.id.track_author_tv)
        val trackDurationTV = findViewById<TextView>(R.id.track_duration_tv)
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
        durationInfoTV.text = dateFormat.format(track.trackTimeMillis)
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
    companion object{
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
    }
}