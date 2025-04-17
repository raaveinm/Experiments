package com.raaveinm.myapplication.dbsystem

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Track::class], version = 1, exportSchema = false)
abstract class TrackDB: RoomDatabase() {
    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var instance: TrackDB? = null

        //  synchronized{} blocks the code inside it to avoid the race condition.
        fun getDatabase(context: Context): TrackDB = instance ?: synchronized(this) {
            Room.databaseBuilder(context, TrackDB::class.java, "track_database" )
                .build().also { instance = it }
        }
    }
}