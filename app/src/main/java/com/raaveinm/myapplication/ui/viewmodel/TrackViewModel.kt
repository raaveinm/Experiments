package com.raaveinm.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.raaveinm.myapplication.dbsystem.Track
import com.raaveinm.myapplication.dbsystem.TrackDB
import com.raaveinm.myapplication.dbsystem.TrackDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.abs

data class TrackInputState(
    val trackName: String = "",
    val artist: String = "",
    val album: String = "",
    val durationSeconds: String = ""
)

/**
 * ViewModel to retrieve, insert, and delete tracks from the [TrackDao].
 */
class TrackViewModel(private val trackDao: TrackDao) : ViewModel() {

    var trackInputState by mutableStateOf(TrackInputState())
        private set
    fun getAllTracks(): Flow<List<Track>> = trackDao.getAll()
    fun getTracksByName(name: String): Flow<List<Track>> = trackDao.getByName(name)

    fun insertTrack() {
        if (validateInput()) {
            viewModelScope.launch {
                val durationMillis = (trackInputState.durationSeconds.toLongOrNull() ?: 0L) * 1000L
                trackDao.insert(
                    Track(
                        track = trackInputState.trackName.trim(),
                        artist = trackInputState.artist.trim(),
                        album = trackInputState.album.trim(),
                        duration = abs(durationMillis)
                    )
                )
                clearInputFields()
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            trackDao.delete(track)
        }
    }
    fun updateInputState(newState: TrackInputState) {
        trackInputState = newState
    }

    fun clearInputFields() {
        trackInputState = TrackInputState()
    }

    private fun validateInput(): Boolean {
        val durationLong = trackInputState.durationSeconds.toLongOrNull()
        return trackInputState.trackName.isNotBlank() &&
                trackInputState.artist.isNotBlank() &&
                trackInputState.album.isNotBlank() &&
                trackInputState.durationSeconds.isNotBlank() &&
                durationLong != null
    }
    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                TrackViewModel(TrackDB.getDatabase(application).trackDao())
            }
        }
    }
}
