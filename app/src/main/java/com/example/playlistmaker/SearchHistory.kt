package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    fun write(input: Track){
        var currentSearchHistory = read()
        currentSearchHistory = currentSearchHistory.filter { it.trackId!=input.trackId }.toMutableList()
        if (currentSearchHistory.size==10){
            currentSearchHistory.removeAt(currentSearchHistory.lastIndex)
        }
        currentSearchHistory.add(0,input)
        clear()
        sharedPreferences
            .edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(currentSearchHistory))
            .apply()
    }
    fun clear(){
        sharedPreferences
            .edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }
    fun read(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, object: TypeToken<List<Track>>() {}.type)
    }
    companion object{
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}