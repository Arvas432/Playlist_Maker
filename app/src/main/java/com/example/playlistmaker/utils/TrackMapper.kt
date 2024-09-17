package com.example.playlistmaker.utils

import com.example.playlistmaker.data.db.entity.PlaylistsTrackPoolEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMapper {
    fun mapTrackDtoToTrack(dto: TrackDto, favoritesIds: Set<Int>): Track{
        return Track(
            dto.trackId,
            dto.trackName.orEmpty(),
            dto.artistName.orEmpty(),
            dto.trackTimeMillis,
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
            track.trackTimeMillis,
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
             entity.trackTimeMillis,
             entity.artworkUrl100.orEmpty(),
             entity.collectionName.orEmpty(),
             entity.releaseDate.orEmpty(),
             entity.primaryGenreName.orEmpty(),
             entity.country.orEmpty(),
             entity.previewUrl.orEmpty()
         )
    }
    fun mapTrackPoolEntityToModel(entity: PlaylistsTrackPoolEntity, favoritesIds: Set<Int>): Track{
        return Track(
            entity.trackId,
            entity.trackName.orEmpty(),
            entity.artistName.orEmpty(),
            entity.trackTimeMillis,
            entity.artworkUrl100.orEmpty(),
            entity.collectionName.orEmpty(),
            entity.releaseDate.orEmpty(),
            entity.primaryGenreName.orEmpty(),
            entity.country.orEmpty(),
            entity.previewUrl.orEmpty(),
            favoritesIds.contains(entity.trackId)
        )
    }
    fun mapModelToTrackPoolEntity(track: Track): PlaylistsTrackPoolEntity{
        return PlaylistsTrackPoolEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}