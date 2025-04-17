package com.raaveinm.myapplication.dbsystem

import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun insert(track: Track)
    suspend fun update(track: Track)
    suspend fun delete(track: Track)

    fun getAll(): Flow<List<Track>>
    fun getByName(name: String): Flow<List<Track>>
    fun getById(id: Int): Flow<Track>
}