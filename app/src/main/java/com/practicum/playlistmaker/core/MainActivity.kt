package com.practicum.playlistmaker.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.settings.presentation.SettingsActivity
import com.practicum.playlistmaker.mediaLibrary.presentation.MediaLibraryActivity

class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchButtonIntent =
                    SearchActivity.createSearchActivityIntent(this@MainActivity)
                startActivity(searchButtonIntent)
            }
        }
        binding.searchButton.setOnClickListener(searchButtonClickListener)

        binding.mediaLibraryButton.setOnClickListener {
            val mediaLibraryButtonIntent =
                MediaLibraryActivity.createMediaLibraryActivityIntent(this)
            startActivity(mediaLibraryButtonIntent)
        }

        binding.optionButton.setOnClickListener {
            val optionButtonIntent = SettingsActivity.createSettingsActivityIntent(this)
            startActivity(optionButtonIntent)
        }
    }
}