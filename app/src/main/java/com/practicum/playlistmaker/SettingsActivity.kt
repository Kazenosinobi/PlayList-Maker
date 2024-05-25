package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val imageViewShare = findViewById<ImageView>(R.id.imageViewShare)
        val imageViewSupport = findViewById<ImageView>(R.id.imageViewSupport)
        val imageViewTerms = findViewById<ImageView>(R.id.imageViewTerms)

        backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
        }

        imageViewShare.setOnClickListener {
            val url = Uri.parse(getString(R.string.share_uri))
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
            }
            val chooser = Intent.createChooser(intent, getString(R.string.messenger_choose))
            startActivity(chooser)
        }

        imageViewSupport.setOnClickListener {
            val message = getString(R.string.support_message)
            val subject = getString(R.string.support_subject)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }

        imageViewTerms.setOnClickListener {
            val url = Uri.parse(getString(R.string.terms_uri))
            val intent = Intent(Intent.ACTION_VIEW, url)
            val chooser = Intent.createChooser(intent, getString(R.string.browser_choose))
            startActivity(chooser)
        }
    }
}