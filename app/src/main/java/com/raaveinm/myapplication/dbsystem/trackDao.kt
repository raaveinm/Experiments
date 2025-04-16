package com.raaveinm.myapplication.dbsystem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    suspend fun getAll(): List<Track>
    @Query("SELECT * FROM track WHERE track_name IN (:track)")
    suspend fun loadAllByTrack(track: String): List<Track>
    @Query("SELECT * FROM track WHERE artist_name IN (:artist)")
    suspend fun loadAllByArtist(artist: String): List<Track>

    @Insert
    suspend fun insertAll(vararg track: Track)
    @Delete
    suspend fun delete(vararg track: Track)
}