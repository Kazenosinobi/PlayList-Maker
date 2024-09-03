package com.practicum.playlistmaker.search.ui.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.practicum.playlistmaker.search.domain.models.Track

class TrackAdapter(
    private val onClick: (Track) -> Unit = {},
) : ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
        holder.itemView.setOnClickListener { onClick(track) }
    }
}