package com.plcoding.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AnalyticsDashboardViewModel: ViewModel() {

    var state by mutableStateOf<AnalyticsDashboardState?>(null)
        private set
}