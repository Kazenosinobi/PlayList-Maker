package com.practicum.playlistmaker.settings.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.core.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val interactor by lazy { Creator.provideSettingsInteractor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
        }

        binding.frameLayoutShare.setOnClickListener {
            interactor.share()
        }

        binding.frameLayoutSupport.setOnClickListener {
            interactor.support()
        }

        binding.frameLayoutTerms.setOnClickListener {
            interactor.termsOfUse()
        }

        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

        binding.flThemeSwitcher.setOnClickListener {
            binding.themeSwitcher.isChecked = !binding.themeSwitcher.isChecked
            (applicationContext as App).switchTheme(binding.themeSwitcher.isChecked)
        }
    }

    companion object {
        fun createSettingsActivityIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}