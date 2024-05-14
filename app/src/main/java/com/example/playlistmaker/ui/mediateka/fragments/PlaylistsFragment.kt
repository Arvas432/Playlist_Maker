package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {
    private val playlistsViewModel: PlaylistsFragmentViewModel by viewModel{
        parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistsFragment().apply {}
    }
}