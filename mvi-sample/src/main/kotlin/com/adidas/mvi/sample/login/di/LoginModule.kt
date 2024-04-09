package com.adidas.mvi.sample.login.di

import com.adidas.mvi.Logger
import com.adidas.mvi.sample.login.viewmodel.LoginViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
public class LoginModule {

    @KoinViewModel
    internal fun provideLoginViewModel(
        logger: Logger,
    ): LoginViewModel {
        return LoginViewModel(
            logger = logger
        )
    }
}