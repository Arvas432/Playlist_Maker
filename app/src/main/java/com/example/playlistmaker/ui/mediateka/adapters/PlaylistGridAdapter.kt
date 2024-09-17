package com.example.playlistmaker.ui.mediateka.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistListItemBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.ui.mediateka.viewholders.PlaylistViewHolder

class PlaylistGridAdapter(private val playlists: List<Playlist>, private val action: (Playlist) -> Unit): RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            action(playlists[position])
        }
    }
}