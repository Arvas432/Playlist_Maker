package com.example.playlistmaker.ui.playlistInfo.adapters

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.adapters.TrackListAdapter
import com.example.playlistmaker.ui.search.viewholders.TrackViewHolder

class PlaylistTracksAdapter(tracks: List<Track>, itemOnClick: (Track)-> Unit, private val itemOnLongPress: (Track) -> Unit): TrackListAdapter(tracks, itemOnClick) {
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnLongClickListener {
            itemOnLongPress(tracks[position])
            true
        }
    }
}