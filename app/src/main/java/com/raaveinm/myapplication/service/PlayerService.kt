package com.raaveinm.myapplication.service

import android.app.Service
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import android.os.IBinder
import androidx.media3.common.MediaItem
import android.util.Log
import com.raaveinm.myapplication.service.Companion.RES_ID
import kotlin.text.isNotEmpty


class PlayerService() : Service(){

    private val TAG = "PlayerService"
    private lateinit var player: ExoPlayer



    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this.applicationContext).build()
        player.playWhenReady = true
        Log.i(TAG, "Player prepared")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val action = intent?.action ?: return START_STICKY
        val mediaResId = intent.getIntExtra(RES_ID, 0)
        val track: List<TrackData> = TrackData.getTracks(mediaResId)

        when (action) {
            Companion.ACTION_PLAY -> {
                commandResolve(1,track)
            }
            Companion.ACTION_STOP -> {
                commandResolve(0,track)
            }
        }

        Log.i(TAG, track.toString())
        return START_STICKY
    }

    private fun commandResolve(
        command: Int,
        data: List<TrackData>
    ) {
        val currentTrack = data[1]
        val uri = currentTrack.location

        if (true){
            val mediaItem = MediaItem.fromUri(uri!!)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}