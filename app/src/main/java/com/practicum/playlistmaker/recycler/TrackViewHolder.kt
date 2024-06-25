package com.practicum.playlistmaker.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.Track

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
        textViewTrackTime.text = item.getTrackTime()
        textViewArtistName.invalidate()
        textViewArtistName.requestLayout()
        Glide.with(imageViewAlbum.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.place_holder)
            .fitCenter()
            .transform(RoundedCorners(4))
            .into(imageViewAlbum)
    }
}