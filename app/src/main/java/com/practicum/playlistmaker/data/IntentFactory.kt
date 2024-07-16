package com.practicum.playlistmaker.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.activity.MediaActivity
import com.practicum.playlistmaker.activity.MediaLibraryActivity
import com.practicum.playlistmaker.activity.SearchActivity
import com.practicum.playlistmaker.activity.SettingsActivity

object IntentFactory {
    fun createMediaActivityIntent(context: Context): Intent {
        return Intent(context, MediaActivity::class.java)
    }

    fun createSettingsActivityIntent(context: Context): Intent {
        return Intent(context, SettingsActivity::class.java)
    }

    fun createSearchActivityIntent(context: Context): Intent {
        return Intent(context, SearchActivity::class.java)
    }

    fun createMediaLibraryActivityIntent(context: Context): Intent {
        return Intent(context, MediaLibraryActivity::class.java)
    }
}