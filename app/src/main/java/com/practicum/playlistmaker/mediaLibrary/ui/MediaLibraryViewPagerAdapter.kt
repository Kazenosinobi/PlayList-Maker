package com.practicum.playlistmaker.mediaLibrary.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.mediaLibrary.ui.favourite.FavouriteTracksFragment
import com.practicum.playlistmaker.mediaLibrary.ui.playList.PlayListsFragment

class MediaLibraryViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouriteTracksFragment.newInstance()
            else -> PlayListsFragment.newInstance()
        }
    }
}