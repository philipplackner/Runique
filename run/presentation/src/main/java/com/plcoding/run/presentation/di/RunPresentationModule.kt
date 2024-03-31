package com.plcoding.run.presentation.di

import com.plcoding.run.domain.RunningTracker
import com.plcoding.run.presentation.active_run.ActiveRunViewModel
import com.plcoding.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}