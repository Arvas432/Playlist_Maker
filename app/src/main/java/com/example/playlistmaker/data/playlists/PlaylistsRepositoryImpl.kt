package com.example.playlistmaker.data.playlists

import android.util.Log
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistToTrackRelationshipEntity
import com.example.playlistmaker.data.playlists.local.PlaylistImageStorageHandler
import com.example.playlistmaker.domain.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.TrackMapper
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val storageHandler: PlaylistImageStorageHandler,
    private val mapper: TrackMapper
) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().insertNewPlaylist(mapModelToNewEntity(playlist))
    }

    override fun getPlaylistById(id: Long): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistsDao().getPlaylistById(id)
        emit(mapEntityToModel(playlist))
    }

    override fun getTracksByPlaylistId(playlistId: Long): Flow<List<Track>> = flow {
        val favoriteIds = appDatabase.favoritesDao().getFavoritesIds().toSet()
        val trackIds = appDatabase.playlistsPoolDao().getTrackIdsForPlaylist(playlistId)
        val tracks = trackIds.map {
            mapper.mapTrackPoolEntityToModel(
                appDatabase.playlistsPoolDao().getPlaylistsPoolTrackById(it), favoriteIds
            )
        }
        Log.i("timestamp", System.currentTimeMillis().toString())
        emit(tracks)
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
                        playlist.id
                    )
                )
            } else {
                appDatabase.playlistsDao().updatePlaylist(
                    PlaylistEntity(
                        playlist.name,
                        playlist.description,
                        null,
                        playlist.id
                    )
                )
            }
        }
    }

    override suspend fun deletePlaylistAndTracks(playlistId: Long) {

        Log.i("deleting playlist", playlistId.toString())
        val playlistWithSongs = appDatabase.playlistsDao().getPlaylistWithSongs(playlistId)
        Log.i("deleting playlist", playlistWithSongs.toString())
        playlistWithSongs.tracks.forEach { track ->
            appDatabase.playlistsPoolDao()
                .deleteOneTrackRelationship(playlistId, track.trackId)
            val playlistCount =
                appDatabase.playlistsPoolDao().getPlaylistCountForTrack(track.trackId)
            if (playlistCount == 0) {
                appDatabase.playlistsPoolDao().deleteTrackFromPool(track.trackId)
            }
        }
        appDatabase.playlistsDao().deletePlaylist(playlistId)

    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int) {
        appDatabase.playlistsPoolDao().deleteOneTrackRelationship(playlistId, trackId)
        val playlistCount = appDatabase.playlistsPoolDao().getPlaylistCountForTrack(trackId)
        if (playlistCount == 0) {
            appDatabase.playlistsPoolDao().deleteTrackFromPool(trackId)
        }

    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getAllPlaylists()
        emit(convertEntityList(playlists))
    }

    override suspend fun addTrack(track: Track) {
        appDatabase.playlistsPoolDao().insertNewTrack(mapper.mapModelToTrackPoolEntity(track))
    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ): Flow<Boolean> = flow {
        if (playlist.id != null) {
            Log.i("playlist id", playlist.id.toString())
            addTrack(track)
            appDatabase.playlistsPoolDao().insertPlaylistTrackRelationship(
                PlaylistToTrackRelationshipEntity(playlist.id, track.trackId.toLong())
            )
            emit(true)
        } else {
            Log.i("insertion", "Id is null")
            emit(false)
        }

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
            )
        } else {
            return PlaylistEntity(
                model.name,
                model.description,
                null,
            )
        }
    }

    private suspend fun mapEntityToModel(entity: PlaylistEntity): Playlist {
        val idList = appDatabase.playlistsPoolDao().getTrackIdsForPlaylist(entity.playlistId)
        Log.i("timestamp", System.currentTimeMillis().toString())
        Log.i("playlist songs", "playlist ${entity.name} songs $idList")
        if (entity.imageFilePath != null) {
            val imageUri = storageHandler.readUriFromFilePath(entity.imageFilePath)
            return Playlist(
                entity.name,
                entity.description,
                imageUri,
                idList,
                idList.size,
                entity.playlistId
            )
        } else {
            return Playlist(
                entity.name,
                entity.description,
                null,
                idList,
                idList.size,
                entity.playlistId
            )
        }

    }
}