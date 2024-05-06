package com.example.playlistmaker.ui.mediateka.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.ui.mediateka.adapters.MediatekaViewPagerAdapter
import com.example.playlistmaker.ui.mediateka.view_model.MediatekaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val viewModel by viewModel<MediatekaViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = MediatekaViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}