package com.plcoding.run.presentation.run_overview

sealed interface RunOverviewEvent {
    data object OnLogout : RunOverviewEvent
}