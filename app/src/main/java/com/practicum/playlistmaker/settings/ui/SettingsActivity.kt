package com.practicum.playlistmaker.settings.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private var binding: ActivitySettingsBinding? = null

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
        }

        binding?.frameLayoutShare?.setOnClickListener {
            viewModel.share()
        }

        binding?.frameLayoutSupport?.setOnClickListener {
            viewModel.support()
        }

        binding?.frameLayoutTerms?.setOnClickListener {
            viewModel.termsOfUse()
        }

        binding?.themeSwitcher?.isChecked = viewModel.isDarkTheme()

        binding?.themeSwitcher?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateTheme(isChecked)
        }

        binding?.flThemeSwitcher?.setOnClickListener {
            binding?.themeSwitcher?.isChecked = binding?.themeSwitcher?.isChecked?.not() == true
            viewModel.updateTheme(binding?.themeSwitcher?.isChecked?.not() == true)
        }
    }

    companion object {
        fun createSettingsActivityIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}