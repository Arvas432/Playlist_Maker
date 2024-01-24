package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var searchLayout = findViewById<Button>(R.id.search_button_layout)
        var playlistLayout = findViewById<Button>(R.id.playlist_button_layout)
        var settingsLayout = findViewById<Button>(R.id.settings_button_layout)

        searchLayout.setOnClickListener {
            val navigateToSearchIntent = Intent(this, SearchActivity::class.java)
            startActivity(navigateToSearchIntent)
        }
        val navigateToPlaylistIntent = Intent(this, MediatekaActivity::class.java)
        val playlistOnclickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(navigateToPlaylistIntent)
            }
        }
        playlistLayout.setOnClickListener(playlistOnclickListener)
        settingsLayout.setOnClickListener {
            val navigateToSettingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(navigateToSettingsIntent)
        }

    }
}