package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.frameLayoutShare.setOnClickListener {
            viewModel.share()
        }

        binding.frameLayoutSupport.setOnClickListener {
            viewModel.support()
        }

        binding.frameLayoutTerms.setOnClickListener {
            viewModel.termsOfUse()
        }

        binding.themeSwitcher.isChecked = viewModel.isDarkTheme()

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateTheme(isChecked)
        }

        binding.flThemeSwitcher.setOnClickListener {
            binding.themeSwitcher.isChecked = binding.themeSwitcher.isChecked.not() == true
            viewModel.updateTheme(binding.themeSwitcher.isChecked.not())
        }
    }
}