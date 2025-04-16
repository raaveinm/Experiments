package com.raaveinm.myapplication.dbsystem

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "track")
data class Track (
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val track: String,
    val artist: String,
    val album: String,
    val duration: Long
)