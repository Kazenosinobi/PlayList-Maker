package com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler

import androidx.recyclerview.widget.DiffUtil
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData

class PlayListDiffCallback : DiffUtil.ItemCallback<PlayListData>() {
    override fun areItemsTheSame(
        oldItem: PlayListData,
        newItem: PlayListData
    ): Boolean {
        return oldItem.playListId == newItem.playListId
    }

    override fun areContentsTheSame(
        oldItem: PlayListData,
        newItem: PlayListData
    ): Boolean {
        return oldItem == newItem
    }
}