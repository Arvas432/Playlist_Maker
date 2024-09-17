package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.LocalTrackStorageHandler
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesLocalTrackStorageHandler(private val sharedPreferences: SharedPreferences, private val gson: Gson):
    LocalTrackStorageHandler {
    override fun write(input: Track){
        var currentSearchHistory = read()
        currentSearchHistory = currentSearchHistory.filter { it.trackId!=input.trackId }.toMutableList()
        if (currentSearchHistory.size==10){
            currentSearchHistory.removeAt(currentSearchHistory.lastIndex)
        }
        currentSearchHistory.add(0,input)
        clear()
        sharedPreferences
            .edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(currentSearchHistory))
            .apply()
    }
    override fun clear(){
        sharedPreferences
            .edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }
    override fun read(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return emptyList()
        return gson.fromJson(json, object: TypeToken<List<Track>>() {}.type)
    }
    companion object{
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}