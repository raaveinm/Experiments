package com.raaveinm.myapplication.service

import com.raaveinm.myapplication.R

data class TrackData(
    var name: String,
    var location: Int
) {
    companion object {
        fun getTracks(mediaResId: Int): List<TrackData> {
            return listOf(
                TrackData("Sample 1", R.raw.sample),
                TrackData("Sample 2", R.raw.sample2)
            )
        }
    }
}

object Companion {
    const val ACTION_PLAY = "com.raaveinm.myapplication.PLAY"
    const val ACTION_STOP = "com.raaveinm.myapplication.STOP"
    const val RES_ID = "com.raaveinm.myapplication.EXTRA_MEDIA_RES_ID"
}

