package com.plcoding.wear.app.presentation

import android.app.Application
import com.plcoding.wear.run.presentation.di.runPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class RuniqueApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                runPresentationModule
            )
        }
    }
}