package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlayListsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {

    private val viewModel: PlayListViewModel by viewModel()

    private lateinit var binding: FragmentPlayListsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = PlayListsFragment()
    }

}