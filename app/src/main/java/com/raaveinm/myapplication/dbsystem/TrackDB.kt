package com.raaveinm.myapplication.dbsystem

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [Track::class], version = 1)
abstract class TrackDB: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}