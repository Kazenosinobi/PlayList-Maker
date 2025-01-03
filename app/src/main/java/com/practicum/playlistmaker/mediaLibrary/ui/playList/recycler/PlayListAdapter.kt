package com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.practicum.playlistmaker.databinding.PlayListItemBinding
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData

class PlayListAdapter(
    private val onClick: (PlayListData) -> Unit = {},
) : ListAdapter<PlayListData, PlayListViewHolder>(PlayListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val binding = PlayListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playList = getItem(position)
        holder.bind(playList)
        holder.itemView.setOnClickListener { onClick(playList) }
    }
}