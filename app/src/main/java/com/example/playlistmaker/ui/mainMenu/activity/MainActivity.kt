package com.example.playlistmaker.ui.mainMenu.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.mediateka.activity.MediatekaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Здесь нету функций которые можно было бы делегировать ViewModel
        val searchLayout = findViewById<Button>(R.id.search_button_layout)
        val playlistLayout = findViewById<Button>(R.id.playlist_button_layout)
        val settingsLayout = findViewById<Button>(R.id.settings_button_layout)
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