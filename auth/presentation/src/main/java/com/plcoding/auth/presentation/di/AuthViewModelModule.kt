package com.plcoding.auth.presentation.di

import com.plcoding.auth.presentation.login.LoginViewModel
import com.plcoding.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}