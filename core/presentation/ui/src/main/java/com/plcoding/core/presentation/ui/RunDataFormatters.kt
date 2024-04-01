package com.plcoding.core.presentation.ui

import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format("%02d", totalSeconds / (60 * 60))
    val minutes = String.format("%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format("%02d", (totalSeconds % 60))

    return "$hours:$minutes:$seconds"
}

fun Double.toFormattedKm(): String {
    return "${this.roundToDecimals(1)} km"
}

fun Duration.toFormattedPace(distanceKm: Double): String {
    if(this == Duration.ZERO || distanceKm <= 0.0) {
        return "-"
    }

    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val avgPaceMinutes = secondsPerKm / 60
    val avgPaceSeconds = String.format("%02d", secondsPerKm % 60)

    return "$avgPaceMinutes:$avgPaceSeconds / km"
}

fun Double.toFormattedKmh(): String {
    return "${roundToDecimals(1)} km/h"
}

fun Int.toFormattedMeters(): String {
    return "$this m"
}

private fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)
    return round(this * factor) / factor
}