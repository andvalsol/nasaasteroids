package com.udacity.asteroidradar.models

import com.squareup.moshi.Json

data class PictureOfDayNetwork(
    @Json(name = "media_type") val mediaType: String,
    val url: String,
    val title: String
)

fun PictureOfDayNetwork.toModelObject(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        url = url,
        title = title
    )
}
