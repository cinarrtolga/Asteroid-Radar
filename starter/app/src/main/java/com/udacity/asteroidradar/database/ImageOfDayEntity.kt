package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.ImageOfDayModel

@Entity
data class ImageOfDayEntity(
    @PrimaryKey
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)

fun ImageOfDayEntity.asDomainModel(): ImageOfDayModel {
    return ImageOfDayModel(
        date = date,
        explanation = explanation,
        hdUrl = hdUrl ?: "",
        mediaType = mediaType ?: "",
        serviceVersion = serviceVersion ?: "",
        title = title,
        url = url
    )
}