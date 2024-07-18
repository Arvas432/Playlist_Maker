package com.example.playlistmaker.ui.player.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.SmallPlaylistListItemBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.ui.player.viewholders.SmallPlaylistViewHolder

class PlaylistSmallAdapter (private val playlists: List<Playlist>,  val itemOnClick: (Playlist, Int)-> Unit): RecyclerView.Adapter<SmallPlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPlaylistViewHolder{
        val layoutInspector = LayoutInflater.from(parent.context)
        return SmallPlaylistViewHolder(SmallPlaylistListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: SmallPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener{
            itemOnClick(playlists[position], position)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}