package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity
import com.practicum.playlistmaker.mediaLibrary.ui.MediaLibraryActivity

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