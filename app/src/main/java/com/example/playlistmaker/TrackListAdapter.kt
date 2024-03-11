package com.example.playlistmaker

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackListAdapter(private val tracks: List<Track>, private val searchHistory: SearchHistory) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            searchHistory.write(tracks[position])
            val navigateToPlayerActivity = Intent(holder.itemView.context, PlayerActivity::class.java)
            Log.i("OPENING TRACK", "Opening track with the country: ${tracks[position].country}")
            navigateToPlayerActivity.putExtra(TRACK_PLAYER_KEY,Gson().toJson(tracks[position]))
            holder.itemView.context.startActivity(navigateToPlayerActivity)
        }
    }
    companion object{
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
    }

}