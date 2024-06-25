package com.practicum.playlistmaker.activity

import android.content.Intent
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
        val mediaButton = findViewById<Button>(R.id.mediaButton)
        val optionButton = findViewById<Button>(R.id.optionButton)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchButtonIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchButtonIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        mediaButton.setOnClickListener {
            val mediaButtonIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaButtonIntent)
        }

        optionButton.setOnClickListener {
            val optionButtonIntent = Intent(this, SettingsActivity::class.java)
            startActivity(optionButtonIntent)
        }
    }
}