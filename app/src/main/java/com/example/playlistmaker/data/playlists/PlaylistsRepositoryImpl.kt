package com.example.playlistmaker.data.playlists

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.playlists.local.PlaylistImageStorageHandler
import com.example.playlistmaker.domain.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.playlists.models.PlaylistAdditionResultType
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.TrackMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val storageHandler: PlaylistImageStorageHandler,
    private val gson: Gson,
    private val mapper: TrackMapper
) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().insertNewPlaylist(mapModelToNewEntity(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        if (playlist.id != null) {
            if (playlist.imageFileUri != null) {
                val imagePath = storageHandler.createImageFile(playlist.imageFileUri)
                appDatabase.playlistsDao().updatePlaylist(
                    PlaylistEntity(
                        playlist.name,
                        playlist.description,
                        imagePath,
                        gson.toJson(playlist.trackList),
                        playlist.trackCount,
                        playlist.id
                    )
                )
            } else {
                appDatabase.playlistsDao().updatePlaylist(
                    PlaylistEntity(
                        playlist.name,
                        playlist.description,
                        null,
                        gson.toJson(playlist.trackList),
                        playlist.trackCount,
                        playlist.id
                    )
                )
            }
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getAllPlaylists()
        emit(convertEntityList(playlists))
    }

    override fun getTracksFromListIds(ids: List<Int>): Flow<List<Track>> = flow {
        val trackList = ids.map { id ->
            mapper.mapTrackPoolEntityToModel(
                appDatabase.playlistsPoolDao().getPlaylistsPoolTrackById(id)
            )
        }
        emit(trackList)
    }

    override suspend fun addTrack(track: Track) {
        appDatabase.playlistsPoolDao().insertNewTrack(mapper.mapModelToTrackPoolEntity(track))
    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ): Flow<PlaylistAdditionResultType>  = flow{
        playlist.trackList.add(track.trackId)
        playlist.trackCount = playlist.trackList.size
        addTrack(track)
        updatePlaylist(playlist)
        emit(PlaylistAdditionResultType.SUCCESS)
    }

    private suspend fun convertEntityList(entities: List<PlaylistEntity>): List<Playlist> {
        return entities.map { entity ->
            val output = mapEntityToModel(entity)
            output
        }
    }

    private suspend fun mapModelToNewEntity(model: Playlist): PlaylistEntity {
        if (model.imageFileUri != null) {
            val imagePath = storageHandler.createImageFile(model.imageFileUri)
            return PlaylistEntity(
                model.name,
                model.description,
                imagePath,
                gson.toJson(model.trackList),
                model.trackCount
            )
        } else {
            return PlaylistEntity(
                model.name,
                model.description,
                null,
                gson.toJson(model.trackList),
                model.trackCount
            )
        }
    }

    private suspend fun mapEntityToModel(entity: PlaylistEntity): Playlist {
        if (entity.imageFilePath != null) {
            val imageUri = storageHandler.readUriFromFilePath(entity.imageFilePath)
            return Playlist(
                entity.name,
                entity.description,
                imageUri,
                gson.fromJson(
                    entity.serializedTrackList,
                    object : TypeToken<List<Int>>() {}.type
                ),
                entity.trackCount,
                entity.playlistId
            )
        } else {
            return Playlist(
                entity.name,
                entity.description,
                null,
                gson.fromJson(
                    entity.serializedTrackList,
                    object : TypeToken<List<Int>>() {}.type
                ),
                entity.trackCount,
                entity.playlistId
            )
        }

    }
}