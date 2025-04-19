package com.raaveinm.myapplication.ui.layout

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.raaveinm.myapplication.service.Companion.ACTION_PAUSE
import com.raaveinm.myapplication.service.Companion.ACTION_PLAY
import com.raaveinm.myapplication.service.PlayerService
import com.raaveinm.myapplication.ui.theme.Pink40
import com.raaveinm.myapplication.ui.theme.Purple80


@Composable
fun ButtonRow(
    modifier: Modifier = Modifier
) {
    var playPauseState by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = modifier.fillMaxSize().padding(25.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        var isPlaying: Boolean by rememberSaveable { mutableStateOf(false) }
        val localModifier: Modifier = Modifier
        Button(
            modifier = localModifier
                .shadow(
                    7.dp, CircleShape, true,
                    Color.Yellow, Color.Cyan
                ),
            //.border(3.dp,rainbowBackground(), CircleShape),
            onClick = {
                playPauseState = !playPauseState
                context.playPause(playPauseState)

            }
        ) {
            Image(
                imageVector = if (!playPauseState)(Icons.Filled.Pause)else(Icons.Filled.PlayArrow),
                contentDescription = "dd",
                modifier = localModifier.clip(CircleShape)
            )
        }
        Button(
            modifier = localModifier
                .shadow(7.dp, CircleShape, true)
                .clip(CircleShape),
            onClick = {  }
        ) {
            Image(
                imageVector = Icons.Filled.Brush,
                contentDescription = "dd",
                modifier = localModifier.clip(CircleShape)
            )
        }
        Button(
            modifier = localModifier
                .shadow(9.dp, CircleShape, true, Pink40, Purple80),
            onClick = { isPlaying = startOnlinePlayer(isPlaying = isPlaying) }
        ){
            Image(
                imageVector = Icons.Filled.Explore,
                contentDescription = "dd",
                modifier = localModifier.clip(CircleShape)
            )
        }
    }
}

private fun startOnlinePlayer(isPlaying: Boolean) : Boolean {
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3")
    mediaPlayer.prepare()
    if (isPlaying) mediaPlayer.stop() else mediaPlayer.start()

    mediaPlayer.setOnCompletionListener { mediaPlayer.stop(); mediaPlayer.release() }

    return !isPlaying
}

fun Context.playPause(isPlaying: Boolean) {
    val intent = Intent(this, PlayerService::class.java).apply {
        action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
    }
    ContextCompat.startForegroundService(this, intent)
}
