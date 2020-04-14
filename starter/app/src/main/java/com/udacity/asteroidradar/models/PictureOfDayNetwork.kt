package com.udacity.asteroidradar.models

data class PictureOfDayNetwork(
    val mediaType: String,
    val url: String,
    val title: String
) {

    fun toModelObject() =
        PictureOfDay(
            mediaType = mediaType,
            url = url,
            title = title
        )
}

