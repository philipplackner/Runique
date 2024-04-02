package com.plcoding.core.data.di

import android.content.SharedPreferences
import com.plcoding.core.data.auth.EncryptedSessionStorage
import com.plcoding.core.data.networking.HttpClientFactory
import com.plcoding.core.data.run.OfflineFirstRunRepository
import com.plcoding.core.domain.SessionStorage
import com.plcoding.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}