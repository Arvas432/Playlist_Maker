package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.ui.mediateka.adapters.MediatekaViewPagerAdapter
import com.example.playlistmaker.ui.mediateka.view_model.MediatekaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaFragment : BindingFragment<FragmentMediatekaBinding>() {
    private lateinit var tabMediator: TabLayoutMediator
    private val viewModel by viewModel<MediatekaViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediatekaBinding {
        return FragmentMediatekaBinding.inflate(inflater, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediatekaViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }
    companion object{
        @JvmStatic
        fun newInstance() = MediatekaFragment.apply {  }

    }
}