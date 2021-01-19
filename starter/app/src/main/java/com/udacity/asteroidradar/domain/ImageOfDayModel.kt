package com.udacity.asteroidradar.domain

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.ImageOfDayEntity
import java.io.Serializable

data class ImageOfDayModel(
    val date: String,
    val explanation: String,
    @Json(name = "hdurl")
    val hdUrl: String,
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "service_version")
    val serviceVersion: String,
    val title: String,
    val url: String
): Serializable

fun ImageOfDayModel.asDatabaseModel(): ImageOfDayEntity {
    return ImageOfDayEntity(
        date = date,
        explanation = explanation ?: "",
        hdUrl = hdUrl ?: "",
        mediaType = mediaType ?: "",
        serviceVersion = serviceVersion ?: "",
        title = title,
        url = url
    )
}