package com.plcoding.runique

import android.app.Application
import com.plcoding.auth.data.di.authDataModule
import com.plcoding.auth.presentation.di.authViewModelModule
import com.plcoding.core.data.di.coreDataModule
import com.plcoding.core.database.di.databaseModule
import com.plcoding.run.location.di.locationModule
import com.plcoding.run.presentation.di.runPresentationModule
import com.plcoding.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule
            )
        }
    }
}