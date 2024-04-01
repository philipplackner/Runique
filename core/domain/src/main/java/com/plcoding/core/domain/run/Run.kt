package com.plcoding.core.domain.run

import com.plcoding.core.domain.location.Location
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id: String?, // null if new run
    val duration: Duration,
    val dateTimeUtc: ZonedDateTime,
    val distanceMeters: Int,
    val location: Location,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
) {
    val avgSpeedKmh: Double
        get() = (distanceMeters / 1000.0) / duration.toDouble(DurationUnit.HOURS)
}
