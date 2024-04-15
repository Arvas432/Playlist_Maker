package com.example.playlistmaker.ui.search.adapters

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemViewBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.viewholders.TrackViewHolder
import com.google.gson.Gson

class TrackListAdapter(private val tracks: List<Track>, private val viewModel: SearchViewModel) : RecyclerView.Adapter<TrackViewHolder>() {
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

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
            if(clickDebounce()){
                viewModel.writeToHistory(tracks[position])
                val navigateToPlayerActivity = Intent(holder.itemView.context, PlayerActivity::class.java)
                navigateToPlayerActivity.putExtra(TRACK_PLAYER_KEY,Gson().toJson(tracks[position]))
                holder.itemView.context.startActivity(navigateToPlayerActivity)
            }
        }
    }
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    companion object{
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}