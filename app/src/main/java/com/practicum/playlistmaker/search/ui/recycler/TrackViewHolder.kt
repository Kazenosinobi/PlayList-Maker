package com.practicum.playlistmaker.search.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.domain.models.Track

class TrackViewHolder(private val binding: TrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        val cornerRadius = binding.root.context.resources.getDimensionPixelSize(R.dimen._4dp)
        with(binding) {
            textViewTrackName.text = item.trackName
            textViewArtistName.text = item.artistName
            textViewTrackTime.text = item.getTrackTime()
            textViewArtistName.invalidate()
            textViewArtistName.requestLayout()

            Glide.with(imageViewAlbum.context)
                .load(item.coverArtworkMini)
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(cornerRadius))
                .into(imageViewAlbum)
        }
    }
}