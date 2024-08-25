package com.practicum.playlistmaker.search.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.practicum.playlistmaker.search.domain.models.Track

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}