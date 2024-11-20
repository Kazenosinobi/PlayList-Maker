package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : Fragment() {
    private var binding: FragmentMediaLibraryBinding? = null

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.viewPager?.adapter = MediaLibraryViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
        )

        tabMediator =
            binding?.tabLayout?.let {
                binding?.viewPager?.let { it1 ->
                    TabLayoutMediator(it, it1) { tab, position ->
                        when (position) {
                            0 -> tab.text = getString(R.string.favourite_tracks)
                            else -> tab.text = getString(R.string.play_lists)
                        }
                    }
                }
            }
        tabMediator?.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
    }
}