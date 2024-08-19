package com.practicum.playlistmaker.settings.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.core.Creator

class SettingsActivity : AppCompatActivity() {
    private val interactor by lazy { Creator.provideSettingsInteractor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val frameLayoutShare = findViewById<FrameLayout>(R.id.frameLayoutShare)
        val frameLayoutSupport = findViewById<FrameLayout>(R.id.frameLayoutSupport)
        val frameLayoutTerms = findViewById<FrameLayout>(R.id.frameLayoutTerms)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val flThemeSwitcher = findViewById<FrameLayout>(R.id.flThemeSwitcher)

        backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
        }

        frameLayoutShare.setOnClickListener {
            interactor.share()
        }

        frameLayoutSupport.setOnClickListener {
            interactor.support()
        }

        frameLayoutTerms.setOnClickListener {
            interactor.termsOfUse()
        }

        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

        flThemeSwitcher.setOnClickListener {
            themeSwitcher.isChecked = !themeSwitcher.isChecked
            (applicationContext as App).switchTheme(themeSwitcher.isChecked)
        }
    }

    companion object {
        fun createSettingsActivityIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}