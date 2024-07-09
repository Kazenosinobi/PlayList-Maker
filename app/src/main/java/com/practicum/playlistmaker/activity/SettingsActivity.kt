package com.practicum.playlistmaker.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.application.App

class SettingsActivity : AppCompatActivity() {
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
            val url = getString(R.string.share_uri)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
            }
            val chooser = Intent.createChooser(intent, getString(R.string.messenger_choose))
            startActivity(chooser)
        }

        frameLayoutSupport.setOnClickListener {
            val message = getString(R.string.support_message)
            val subject = getString(R.string.support_subject)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }

        frameLayoutTerms.setOnClickListener {
            val url = Uri.parse(getString(R.string.terms_uri))
            val intent = Intent(Intent.ACTION_VIEW, url)
            val chooser = Intent.createChooser(intent, getString(R.string.browser_choose))
            startActivity(chooser)
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
}