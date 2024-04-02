package com.plcoding.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick: AnalyticsAction
}