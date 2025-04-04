package com.raaveinm.myapplication.service

import com.raaveinm.myapplication.R


data class TrackData(
    var location: Int
) {
    companion object {
        fun getTracks(): List<TrackData> {
            return listOf(
                TrackData(R.raw.sample),
                TrackData(R.raw.sample2)
            )
        }
    }
}