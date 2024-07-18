package com.example.playlistmaker.ui.mediateka.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistListItemBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.utils.TrackCountFormatter


class PlaylistViewHolder(private val binding: PlaylistListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Playlist) {
        val trackFormatter = TrackCountFormatter(itemView.context)
        binding.playlistNameTv.text = model.name
        binding.playlistDescriptionTv.text = trackFormatter.formatTrackCount(model.trackCount)
        if (model.imageFileUri == null) {
            binding.playlistIv.setImageResource(R.drawable.placeholder)
        } else {
            binding.playlistIv.setImageURI(model.imageFileUri)
        }
    }
}