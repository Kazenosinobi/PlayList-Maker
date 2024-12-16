package com.practicum.playlistmaker.playListCreate.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlayListCreateBinding

class PlayListCreateFragment : Fragment() {

    private var binding: FragmentPlayListCreateBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayListCreateBinding.inflate(inflater, container, false)
        return binding?.root
    }
}