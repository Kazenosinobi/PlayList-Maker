package com.practicum.playlistmaker.mediaLibrary.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                else -> tab.text = getString(R.string.play_lists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    companion object {

        fun createMediaLibraryActivityIntent(context: Context): Intent {
            return Intent(context, MediaLibraryActivity::class.java)
        }
    }
}