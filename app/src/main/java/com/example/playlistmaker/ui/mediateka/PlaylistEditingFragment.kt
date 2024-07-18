package com.example.playlistmaker.ui.mediateka

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistEditingViewModel
import com.example.playlistmaker.ui.playlistInfo.fragments.PlaylistInfoFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditingFragment: PlaylistCreationFragment() {
    override val viewModel by viewModel<PlaylistEditingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner){
            binding.playlistTitleEt.setText(it.name)
            binding.playlistDescriptionEt.setText(it.description)
            if (it.imageFileUri!=null){
                binding.playlistImageIv.setImageURI(it.imageFileUri)
            } else{
                binding.playlistImageIv.setImageResource(R.drawable.placeholder)
            }
        }
        binding.screenTitleTv.text = getString(R.string.edit)
        binding.createPlaylistBtn.text = getString(R.string.save)
        val playlistId = requireArguments().getLong(PlaylistInfoFragment.PLAYLIST_EDIT_KEY)
        viewModel.requestPlaylistInfo(playlistId)

    }
    override fun handleBackPress(){
        findNavController().navigateUp()
    }

    override fun handleUriChange(uri: Uri?) {
        if (uri != null) {
            binding.playlistImageIv.setImageURI(uri)
            viewModel.setUri(uri)
        }
    }
    override fun createButtonFunction(){
        viewModel.finalizePlaylistAction()
        findNavController().navigateUp()
    }
}