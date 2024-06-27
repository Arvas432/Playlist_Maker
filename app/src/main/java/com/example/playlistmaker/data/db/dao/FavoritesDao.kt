package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackIntoFavorites(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackFromFavorites(track: TrackEntity)

    @Query("SELECT * FROM favorites_table")
    suspend fun getFavorites(): List<TrackEntity>

    @Query("SELECT track_id FROM favorites_table")
    suspend fun getFavoritesIds(): List<Int>
}