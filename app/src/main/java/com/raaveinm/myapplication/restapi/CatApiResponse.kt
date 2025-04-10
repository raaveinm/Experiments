package com.raaveinm.myapplication.restapi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatApiResponse(
    @Json(name = "url") val relativeUrl: String
)