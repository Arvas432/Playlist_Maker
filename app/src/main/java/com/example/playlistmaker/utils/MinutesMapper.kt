package com.example.playlistmaker.utils

import android.content.Context
import com.example.playlistmaker.R
class MinutesMapper(val context: Context) {
    fun formatMinutes(minutes: Int): String {
        return when {
            minutes % 100 in 11..19 -> context.getString(R.string.from_11_to_19_mins, minutes.toString())
            minutes % 10 == 1 -> context.getString(R.string.one_minute, minutes.toString())
            minutes % 10 in 2..4 -> context.getString(R.string.two_to_four_minutes, minutes.toString())
            else -> context.getString(R.string.few_minutes, minutes.toString())
        }
    }
}