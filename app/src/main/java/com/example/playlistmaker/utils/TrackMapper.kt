package com.example.playlistmaker.utils

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMapper {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun mapTrackDtoToTrack(dto: TrackDto, favoritesIds: Set<Int>): Track{
        return Track(
            dto.trackId,
            dto.trackName.orEmpty(),
            dto.artistName.orEmpty(),
            dateFormat.format(dto.trackTimeMillis),
            dto.artworkUrl100.orEmpty(),
            dto.collectionName.orEmpty(),
            dto.releaseDate.orEmpty(),
            dto.primaryGenreName.orEmpty(),
            dto.country.orEmpty(),
            dto.previewUrl.orEmpty(),
            favoritesIds.contains(dto.trackId)
        )
    }
    fun mapModelToEntity(track: Track): TrackEntity{
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            milisecondsFromDate(track.formattedDuration),
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
    fun mapEntityToModel(entity: TrackEntity): Track{
         return Track(
             entity.trackId,
             entity.trackName.orEmpty(),
             entity.artistName.orEmpty(),
             dateFormat.format(entity.trackTimeMillis),
             entity.artworkUrl100.orEmpty(),
             entity.collectionName.orEmpty(),
             entity.releaseDate.orEmpty(),
             entity.primaryGenreName.orEmpty(),
             entity.country.orEmpty(),
             entity.previewUrl.orEmpty()
         )
    }
    fun milisecondsFromDate(formattedDuration: String): Long? {
        return try {
            val date = dateFormat.parse(formattedDuration)
            date.time
        } catch (e: Exception){
            null
        }
    }
}