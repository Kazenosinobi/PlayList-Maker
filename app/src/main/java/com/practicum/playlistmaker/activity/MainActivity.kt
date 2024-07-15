package com.practicum.playlistmaker.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.IntentFactory

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchButton)
        val mediaLibraryButton = findViewById<Button>(R.id.mediaLibraryButton)
        val optionButton = findViewById<Button>(R.id.optionButton)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchButtonIntent = IntentFactory.createSearchActivityIntent(this@MainActivity)
                startActivity(searchButtonIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        mediaLibraryButton.setOnClickListener {
            val mediaLibraryButtonIntent = IntentFactory.createMediaLibraryActivityIntent(this)
            startActivity(mediaLibraryButtonIntent)
        }

        optionButton.setOnClickListener {
            val optionButtonIntent = IntentFactory.createSettingsActivityIntent(this)
            startActivity(optionButtonIntent)
        }
    }
}