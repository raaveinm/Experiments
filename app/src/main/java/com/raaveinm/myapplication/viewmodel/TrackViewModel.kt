package com.raaveinm.myapplication.viewmodel
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import com.raaveinm.myapplication.dbsystem.Track
//import com.raaveinm.myapplication.dbsystem.TrackRepository
//
//class TrackViewModel(private val trackRepository: TrackRepository) : ViewModel() {
//    var trackUIState by mutableStateOf(TrackUIState())
//        private set
//
//    fun updateUiState(track: Track) {
//        TrackUiState(trackDetails = )
//    }
//    suspend fun addTrack(track: Track) = trackRepository.insert(track)
//
//}