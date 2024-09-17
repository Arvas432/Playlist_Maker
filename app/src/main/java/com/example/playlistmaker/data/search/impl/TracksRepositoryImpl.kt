package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.dto.ITunesResponse
import com.example.playlistmaker.data.search.dto.ITunesSearchRequest
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.ServerResponseType
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val appDatabase: AppDatabase, private val trackMapper: TrackMapper): TracksRepository {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>, ServerResponseType>> = flow{
        val response = networkClient.doRequest(ITunesSearchRequest(expression))
        val favoriteIds = appDatabase.favoritesDao().getFavoritesIds().toSet()
        when(response.resultCode){
            200 ->{
                with(response as ITunesResponse){
                    val results = results.map {
                       trackMapper.mapTrackDtoToTrack(it, favoriteIds)
                    }
                    emit(Pair(results, ServerResponseType.SUCCESS))
                }
            } else ->{
                emit(Pair(emptyList(), ServerResponseType.ERROR))
            }
        }
    }
}