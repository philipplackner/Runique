package com.plcoding.auth.data.di

import com.plcoding.auth.data.AuthRepositoryImpl
import com.plcoding.auth.data.EmailPatternValidator
import com.plcoding.auth.domain.AuthRepository
import com.plcoding.auth.domain.PatternValidator
import com.plcoding.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}