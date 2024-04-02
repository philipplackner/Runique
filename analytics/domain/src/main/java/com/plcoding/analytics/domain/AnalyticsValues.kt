package com.plcoding.analytics.domain

import kotlin.time.Duration

data class AnalyticsValues(
    val totalDistanceRun: Int = 0,
    val totalTimeRun: Duration = Duration.ZERO,
    val fastestEverRun: Double = 0.0,
    val avgDistancePerRun: Double = 0.0,
    val avgPacePerRun: Double = 0.0
)
