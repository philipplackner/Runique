package com.plcoding.runique

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object Intro : Routes

    @Serializable
    data object Auth : Routes

    @Serializable
    data object Register : Routes

    @Serializable
    data object Login : Routes

    @Serializable
    data object Run : Routes

    @Serializable
    data object RunOverview : Routes

    @Serializable
    data object ActiveRun : Routes
}