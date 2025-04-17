package com.raaveinm.myapplication.dbsystem

import kotlinx.coroutines.flow.Flow

class OfflineTrackRepository(private val trackDao: TrackDao) : TrackRepository  {

    override suspend fun insert(track: Track) = trackDao.insert(track)

    override suspend fun update(track: Track) = trackDao.update(track)

    override suspend fun delete(track: Track) = trackDao.delete(track)

    override fun getAll(): Flow<List<Track>> = trackDao.getAll()

    override fun getByName(name: String): Flow<List<Track>> = trackDao.getByName(name)

    override fun getById(id: Int): Flow<Track> = trackDao.getById(id)
}