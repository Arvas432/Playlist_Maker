package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.ui.mediateka.adapters.PlaylistGridAdapter
import com.example.playlistmaker.ui.mediateka.states.PlaylistsState
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {
    private val playlistsViewModel: PlaylistsFragmentViewModel by viewModel<PlaylistsFragmentViewModel>()
    private lateinit var adapter: PlaylistGridAdapter
    private var playlists = ArrayList<Playlist>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_playlistCreationFragment)
        }
        playlistsViewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
        playlistsViewModel.loadPlaylists()
        adapter = PlaylistGridAdapter(playlists)
        binding.playlistsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsRv.adapter = adapter
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.loadPlaylists()
    }

    private fun setEmptyScreenState() {
        binding.playlistsRv.isVisible = false
        binding.errorPlaceholderImage.isVisible = true
        binding.errorText.isVisible = true
    }

    private fun setContentScreenState(content: List<Playlist>) {
        playlists.clear()
        playlists.addAll(content)
        adapter.notifyDataSetChanged()
        binding.playlistsRv.isVisible = true
        binding.errorPlaceholderImage.isVisible = false
        binding.errorText.isVisible = false
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Default -> Unit
            is PlaylistsState.Content -> setContentScreenState(state.playlists)
            is PlaylistsState.Empty -> setEmptyScreenState()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistsFragment().apply {}
    }
}