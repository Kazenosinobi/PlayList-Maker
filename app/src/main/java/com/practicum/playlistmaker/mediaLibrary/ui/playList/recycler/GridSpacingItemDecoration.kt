package com.practicum.playlistmaker.mediaLibrary.ui.playList.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

class GridSpacingItemDecoration(
    private val context: Context
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing = context.resources.getDimensionPixelSize(R.dimen._8dp)

        val left: Int = spacing
        val right: Int = spacing
        val top: Int = spacing
        val bottom: Int = spacing

        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(left, top, right, bottom)
    }
}


