package com.raaveinm.myapplication.service

import android.annotation.SuppressLint
import android.app.job.JobInfo.PRIORITY_LOW
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.raaveinm.myapplication.R
import com.raaveinm.myapplication.ui.layout.notificationChannelTwoId

class NotificationReceiver(): BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        Log.d("RRR", "onReceive: notification")
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            context!!, notificationChannelTwoId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hello")
            .setContentText("Hello From application after " +
                    "${(intent?.getLongExtra("time", 0L))?.div((1000L))} s")
            .setPriority(PRIORITY_LOW)
        with(NotificationManagerCompat.from(context)) {
            notify(2, notification.build())
        }
    }
}
