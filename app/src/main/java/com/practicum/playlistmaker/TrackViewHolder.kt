package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
) {
    private val imageViewAlbum: ImageView = itemView.findViewById(R.id.imageViewAlbum)
    private val textViewTrackName: TextView = itemView.findViewById(R.id.textViewTrackName)
    private val textViewArtistName: TextView = itemView.findViewById(R.id.textViewArtistName)
    private val textViewTrackTime: TextView = itemView.findViewById(R.id.textViewTrackTime)

    fun bind(item: Track) {
        textViewTrackName.text = item.trackName
        textViewArtistName.text = item.artistName
        textViewTrackTime.text = item.trackTime
        Glide.with(imageViewAlbum.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.cap)
            .fitCenter()
            .transform(RoundedCorners(4))
            .into(imageViewAlbum)
    }
}