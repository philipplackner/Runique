package com.plcoding.run.data.di

import com.plcoding.run.data.CreateRunWorker
import com.plcoding.run.data.DeleteRunWorker
import com.plcoding.run.data.FetchRunsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
}