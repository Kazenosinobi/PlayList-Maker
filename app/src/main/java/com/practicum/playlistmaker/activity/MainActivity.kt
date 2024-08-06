package com.practicum.playlistmaker.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchButton)
        val mediaLibraryButton = findViewById<Button>(R.id.mediaLibraryButton)
        val optionButton = findViewById<Button>(R.id.optionButton)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchButtonIntent =
                    SearchActivity.createSearchActivityIntent(this@MainActivity)
                startActivity(searchButtonIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        mediaLibraryButton.setOnClickListener {
            val mediaLibraryButtonIntent =
                MediaLibraryActivity.createMediaLibraryActivityIntent(this)
            startActivity(mediaLibraryButtonIntent)
        }

        optionButton.setOnClickListener {
            val optionButtonIntent = SettingsActivity.createSettingsActivityIntent(this)
            startActivity(optionButtonIntent)
        }
    }
}