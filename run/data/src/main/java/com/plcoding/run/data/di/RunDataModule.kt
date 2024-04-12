package com.plcoding.run.data.di

import com.plcoding.core.domain.run.SyncRunScheduler
import com.plcoding.run.data.CreateRunWorker
import com.plcoding.run.data.DeleteRunWorker
import com.plcoding.run.data.FetchRunsWorker
import com.plcoding.run.data.SyncRunWorkerScheduler
import com.plcoding.run.data.connectivity.PhoneToWatchConnector
import com.plcoding.run.domain.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}