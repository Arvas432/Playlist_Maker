package com.example.playlistmaker.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemViewBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.viewholders.TrackViewHolder

open class TrackListAdapter(protected val tracks: List<Track>, val itemOnClick: (Track)-> Unit) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            itemOnClick(tracks[position])
        }
    }


}