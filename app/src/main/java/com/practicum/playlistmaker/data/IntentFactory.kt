package com.practicum.playlistmaker.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.activity.MediaActivity

object IntentFactory {
    fun createMediaActivityIntent(context: Context): Intent {
        return Intent(context, MediaActivity::class.java)
    }
}