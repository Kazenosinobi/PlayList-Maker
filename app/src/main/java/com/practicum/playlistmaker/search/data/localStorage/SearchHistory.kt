package com.practicum.playlistmaker.search.data.localStorage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.models.Track

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    fun getSearchHistory(): Array<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun saveSearchTrackHistory(tracks: Array<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs
            .edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "key_for_search_history"
    }
}