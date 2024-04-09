package com.adidas.mvi.sample.app.di

import com.adidas.mvi.Logger
import com.adidas.mvi.sample.app.logger.ReducerConsoleLogger
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
public class AppModule {

    @Single
    internal fun provideLogger(): Logger {
        return ReducerConsoleLogger()
    }
}