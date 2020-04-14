package com.udacity.asteroidradar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AsteroidDatabase(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) {

    fun toDomainModel() =
        Asteroid(
            id = id,
            codename = codename,
            closeApproachDate = closeApproachDate,
            absoluteMagnitude = absoluteMagnitude,
            estimatedDiameter = estimatedDiameter,
            relativeVelocity = relativeVelocity,
            distanceFromEarth = distanceFromEarth,
            isPotentiallyHazardous = isPotentiallyHazardous
        )

    fun List<AsteroidDatabase>.toListDomainModel() =
        this.map {
            this@AsteroidDatabase.toDomainModel()
        }
}