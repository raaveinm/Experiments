package com.raaveinm.myapplication.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import android.os.IBinder
import androidx.media3.common.MediaItem
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.raaveinm.myapplication.R
import kotlin.text.isNotEmpty

const val CHANNEL_ID = "PlayerServiceChannel"

object Companion {
    const val ACTION_PLAY = "com.raaveinm.myapplication.PLAY"
    const val ACTION_PAUSE = "com.raaveinm.myapplication.PAUSE"
}

class PlayerService() : Service(){

    private val tag = "PlayerService"
    private lateinit var player: ExoPlayer
    private lateinit var notification: Notification

    private var trackList: List<TrackData> = TrackData.getTracks()
    private var currentTrackIndex: Int = 0

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this.applicationContext).build()
        player.addListener(PlayerListener())
        createNotificationChannel()
        notification = createNotification()

        player.playWhenReady = true
        Log.i(tag, "Player prepared")
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            Companion.ACTION_PLAY -> {
                startForeground(1,notification)
                commandResolve(1)
            }
            Companion.ACTION_PAUSE -> {
                commandResolve(0)
            }
        }

        Log.i(tag, trackList.toString())
        return START_STICKY
    }

    private fun commandResolve(
        command: Int
    ) {
        val currentTrack = trackList[currentTrackIndex]
        val currentPackageName = applicationContext.packageName
        val uri = "android.resource://$currentPackageName/${currentTrack.location}"

        if (uri.isNotEmpty()){
            val mediaItem = MediaItem.fromUri(uri)
            when (command) {
                0 -> {
                    player.pause()
                }
                1 -> {
                    val currentMediaItem = player.currentMediaItem

                    if (currentMediaItem == null || currentMediaItem.mediaId != mediaItem.mediaId ||
                            !player.isCommandAvailable(ExoPlayer.COMMAND_PLAY_PAUSE)){
                        player.setMediaItem(mediaItem)
                        player.prepare()
                    }
                    player.play()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::player.isInitialized) {
            player.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Player Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(): Notification{
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(tag)
            .setContentText("Playing audio in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        return notification
    }

    inner class PlayerListener : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.e(tag, "ExoPlayer error: ${error.message}")
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_ENDED) {
                currentTrackIndex = (currentTrackIndex + 1) % trackList.size
                commandResolve(1)
            }
        }
    }
}