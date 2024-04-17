package com.plcoding.analytics.presentation.di

import com.plcoding.analytics.presentation.AnalyticsDashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val analyticsPresentationModule = module {
    viewModelOf(::AnalyticsDashboardViewModel)
}