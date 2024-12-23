package com.practicum.playlistmaker.playListBottomSheet.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlayListBottomSheetItemBinding
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

class PlayListBottomSheetViewHolder(private val binding: PlayListBottomSheetItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PlayListCreateData) {
        val cornerRadius = binding.root.context.resources.getDimensionPixelSize(R.dimen._4dp)
        with(binding) {
            TextViewName.text = item.nameOfAlbum
            TextViewTracksCount.text = getTrackCountText(item.tracks.size)

            Glide.with(imageViewAlbum.context)
                .load(item.image)
                .placeholder(R.drawable.place_holder)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .into(imageViewAlbum)
        }
    }

    private fun getTrackCountText(trackCount: Int): String {
        val resources = binding.root.context.resources
        return resources.getQuantityString(R.plurals.tracks_count, trackCount.toInt(), trackCount)
    }
}