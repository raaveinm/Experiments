package com.raaveinm.myapplication.ui.layout

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.annotation.SuppressLint
import android.app.job.JobInfo.PRIORITY_LOW
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.util.NotificationUtil.IMPORTANCE_DEFAULT
import androidx.media3.common.util.NotificationUtil.createNotificationChannel
import androidx.media3.common.util.UnstableApi
import com.raaveinm.myapplication.service.NotificationReceiver
import com.raaveinm.myapplication.R

const val notificationChannelOneId = "channel_1"
const val notificationChannelTwoId = "channel_2"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(UnstableApi::class)
@Composable
fun NotificationsMain(
    @SuppressLint("ModifierParameter") scaffoldModifier: Modifier,
    context: Context,
){
    createNotificationChannel(
        context,
        notificationChannelOneId,
        R.string.channel_one_name,
        R.string.channel_one_description,
        IMPORTANCE_DEFAULT
    )

    createNotificationChannel(
        context,
        notificationChannelTwoId,
        R.string.channel_two_name,
        R.string.channel_two_description,
        IMPORTANCE_DEFAULT
    )

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
            onResult = { isGranted -> if (isGranted) {Log.d("permission", "Permission Granted")}}
    )
    Column (
        modifier = scaffoldModifier.fillMaxSize()
    ) {
        var time:Long  by rememberSaveable { mutableLongStateOf(0) }
        var text: String by rememberSaveable { mutableStateOf("") }
        val modifier = Modifier
        Button(
            onClick = { sentNotification(context) },
            modifier = modifier
        ) {
            Text(text = "Get Notification RN")
        }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; time = text.toLongOrNull()?.times(1000L) ?: 0L },
            label = { Text("time in seconds") },
            modifier = modifier
        )
        Button(
            onClick = { scheduleNotification(context,time) },
            modifier = modifier
        ) {
            Text(text = "Get Notification Later")
        }
        Button(
            onClick = { checkAndRequestPermission(context, requestPermissionLauncher) },
            modifier = modifier
        ) {
            Text(text = "Check Permission")
        }
    }
}

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun sentNotification(context: Context) {
    val notification: NotificationCompat.Builder = NotificationCompat.Builder(
        context, notificationChannelOneId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Hello")
        .setContentText("Hello From application")
        .setPriority(PRIORITY_LOW)

    with(NotificationManagerCompat.from(context)) {
        notify(1, notification.build())
    }
}

private fun scheduleNotification(context: Context, time: Long) {
    Log.d("RRR", "scheduleNotification: $time")
    val notification: NotificationCompat.Builder = NotificationCompat.Builder(
        context, notificationChannelTwoId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Hello")
        .setContentText("Hello From application after $time ms")

    val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("notification_id", 2)
        putExtra("notification", notification.build())
        putExtra("time", time)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        notificationIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent)
}

private fun checkAndRequestPermission(
    context: Context,
    launcher: ActivityResultLauncher<String>,
) {
    when {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}