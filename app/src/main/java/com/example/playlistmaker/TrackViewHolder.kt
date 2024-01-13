package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(private val parent:ViewGroup,itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)): RecyclerView.ViewHolder(itemView){
    private val trackName: TextView = itemView.findViewById(R.id.track_title)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackAuthor: TextView = itemView.findViewById(R.id.track_author)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)
    fun bind(model: Track){
        trackName.text = model.trackName
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(itemView.context.resources.getDimension(R.dimen.track_image_rounding), itemView.context)))
            .into(trackImage)
        trackAuthor.text = model.artistName
        trackDuration.text = model.trackTime
    }
    //функция из методички, тут нельзя напрямую передавать значение в px поскольку оно зависит от размеров экрана
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}