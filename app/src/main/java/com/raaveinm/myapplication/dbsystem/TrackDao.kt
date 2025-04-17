package com.raaveinm.myapplication.dbsystem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE) suspend fun insert(track: Track)
    @Update suspend fun update(track: Track)
    @Delete suspend fun delete(track: Track)

    @Query ("SELECT * FROM track ORDER BY id ASC")
    fun getAll(): Flow<List<Track>>
    @Query ("SELECT * FROM track WHERE track = :name")
    fun getByName(name: String): Flow<List<Track>>
    @Query ("SELECT * FROM track WHERE id = :id")
    fun getById(id: Int): Flow<Track>
}