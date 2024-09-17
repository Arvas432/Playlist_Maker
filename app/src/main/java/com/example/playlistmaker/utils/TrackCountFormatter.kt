package com.example.playlistmaker.utils

import android.content.Context
import com.example.playlistmaker.R

//Я попробовал использовать вместо этого plurals, но приложение использует английскую локаль и числа 2 - 4 форматируются неправильно, как менять локаль внутри самого приложения я не нашел, оставляю пока так
class TrackCountFormatter(val context: Context) {
    fun formatTrackCount(trackCount: Int): String {
        val remainder10 = trackCount % 10
        val remainder100 = trackCount % 100

        return when {
            remainder100 in 11..19 -> context.getString(
                R.string.number_tracks_11_through_19_russian,
                trackCount.toString()
            )

            remainder10 == 1 -> context.getString(
                R.string.one_track_russian,
                trackCount.toString()
            )

            remainder10 in 2..4 -> context.getString(
                R.string.number_2_through_4_tracks_russian,
                trackCount.toString()
            )

            else -> context.getString(
                R.string.number_tracks_other_russian,
                trackCount.toString()
            )
        }
    }
}