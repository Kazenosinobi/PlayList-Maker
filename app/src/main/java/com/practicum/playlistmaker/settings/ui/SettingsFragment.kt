package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.MTVShare?.setOnClickListener {
            viewModel.share()
        }

        binding?.MTVSupport?.setOnClickListener {
            viewModel.support()
        }

        binding?.MTVTerms?.setOnClickListener {
            viewModel.termsOfUse()
        }

        binding?.themeSwitcher?.isChecked = viewModel.isDarkTheme()

        binding?.themeSwitcher?.setOnClickListener {
            binding?.themeSwitcher?.isChecked?.let { it1 -> viewModel.updateTheme(it1) }
        }
    }
}