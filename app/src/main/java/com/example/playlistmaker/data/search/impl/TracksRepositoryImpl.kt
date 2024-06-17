package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.dto.ITunesResponse
import com.example.playlistmaker.data.search.dto.ITunesSearchRequest
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.ServerResponseType
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun searchTracks(expression: String): Flow<Pair<List<Track>, ServerResponseType>> = flow{
        val response = networkClient.doRequest(ITunesSearchRequest(expression))
        when(response.resultCode){
            200 ->{
                with(response as ITunesResponse){
                    val results = results.map {
                       mapTrackDtoToTrack(it)
                    }
                    emit(Pair(results, ServerResponseType.SUCCESS))
                }
            } else ->{
                emit(Pair(emptyList(), ServerResponseType.ERROR))
            }
        }
    }
    private fun mapTrackDtoToTrack(dto: TrackDto): Track{
        return  Track(
            dto.trackId,
            dto.trackName.orEmpty(),
            dto.artistName.orEmpty(),
            dateFormat.format(dto.trackTimeMillis),
            dto.artworkUrl100.orEmpty(),
            dto.collectionName.orEmpty(),
            dto.releaseDate.orEmpty(),
            dto.primaryGenreName.orEmpty(),
            dto.country.orEmpty(),
            dto.previewUrl.orEmpty()
        )
    }
}