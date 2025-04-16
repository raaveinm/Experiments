package com.raaveinm.myapplication.ui.layout

import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.raaveinm.myapplication.dbsystem.Track
import com.raaveinm.myapplication.dbsystem.TrackDB
import kotlinx.coroutines.Dispatchers
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.withContext

@Composable
fun DBMain(
    modifier: Modifier,
    applicationContext: Context
){
    val db = remember {
        Room.databaseBuilder(
            applicationContext,
            TrackDB::class.java, "database-name"
        )
        .build()
    }

    var tracks by remember { mutableStateOf<List<Track>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(key1 = Unit) {
        isLoading = true
        tracks = withContext(Dispatchers.IO) { db.trackDao().getAll() }
        isLoading = false
        Log.d("DBMain", "Tracks loaded: $tracks")
    }

    LazyColumn {
        item {
            Row {
                Text(text = "id")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "track")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "artist")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "album")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "duration")
            }
        }
        items(tracks.size) { index ->
           TrackItem(tracks[index])
        }
    }
}

@Composable
fun TrackItem(track: Track) {
   Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
   ) {
       Text(
            text = track.id.toString(),
            modifier = Modifier.weight(1f).padding(4.dp),
            style = TextStyle(color = Color.Black)
        )
       Text(
           text = track.track.toString(),
           modifier = Modifier.weight(1f).padding(4.dp),
           style = TextStyle(color = Color.Black)
       )
       Text(
           text = track.artist.toString(),
           modifier = Modifier.weight(1f).padding(4.dp),
           style = TextStyle(color = Color.Black)
       )
       Text(
           text = track.album.toString(),
           modifier = Modifier.weight(1f).padding(4.dp),
           style = TextStyle(color = Color.Black)
       )
       Text(
           text = track.duration.toString(),
           modifier = Modifier.weight(1f).padding(4.dp),
           style = TextStyle(color = Color.Black)
       )
   }
}

@Composable
fun AddToDB(
    modifier: Modifier,
    addTrack: (track: Track) -> Unit
){
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}