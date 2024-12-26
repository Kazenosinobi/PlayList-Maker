package com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler

import androidx.recyclerview.widget.DiffUtil
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData

class PlayListDiffCallback : DiffUtil.ItemCallback<PlayListCreateData>() {
    override fun areItemsTheSame(
        oldItem: PlayListCreateData,
        newItem: PlayListCreateData
    ): Boolean {
        return oldItem.playListId == newItem.playListId
    }

    override fun areContentsTheSame(
        oldItem: PlayListCreateData,
        newItem: PlayListCreateData
    ): Boolean {
        return oldItem == newItem
    }
}