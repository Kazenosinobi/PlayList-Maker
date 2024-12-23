package com.practicum.playlistmaker.playListBottomSheet.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.practicum.playlistmaker.databinding.PlayListBottomSheetItemBinding
import com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler.PlayListDiffCallback
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

class PlayListBottomSheetAdapter(
    private val onClick: (PlayListCreateData) -> Unit = {},
) : ListAdapter<PlayListCreateData, PlayListBottomSheetViewHolder>(PlayListDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayListBottomSheetViewHolder {
        val binding = PlayListBottomSheetItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayListBottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListBottomSheetViewHolder, position: Int) {
        val playList = getItem(position)
        holder.bind(playList)
        holder.itemView.setOnClickListener { onClick(playList) }
    }
}