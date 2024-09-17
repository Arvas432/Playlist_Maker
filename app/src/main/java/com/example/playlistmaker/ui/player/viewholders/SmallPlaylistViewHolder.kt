package com.example.playlistmaker.ui.player.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SmallPlaylistListItemBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.utils.TrackCountFormatter

class SmallPlaylistViewHolder(private val binding: SmallPlaylistListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Playlist) {
        val trackFormatter = TrackCountFormatter(itemView.context)
        binding.playlistTitleTv.text = model.name
        binding.playlistTrackCountTv.text = trackFormatter.formatTrackCount(model.trackCount)
        if (model.imageFileUri == null) {
            binding.playlistIv.setImageResource(R.drawable.placeholder)
        } else {
            binding.playlistIv.setImageURI(model.imageFileUri)
        }
    }
}