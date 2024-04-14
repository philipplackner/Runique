package com.plcoding.runique

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.plcoding.auth.data.di.authDataModule
import com.plcoding.auth.presentation.di.authViewModelModule
import com.plcoding.core.connectivity.data.di.coreConnectivityDataModule
import com.plcoding.core.data.di.coreDataModule
import com.plcoding.core.database.di.databaseModule
import com.plcoding.run.data.di.runDataModule
import com.plcoding.run.location.di.locationModule
import com.plcoding.run.network.di.networkModule
import com.plcoding.run.presentation.di.runPresentationModule
import com.plcoding.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
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
            workManagerFactory()
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule,
                runDataModule,
                coreConnectivityDataModule
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}