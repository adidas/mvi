package com.adidas.mvi.sample.app

import android.app.Application
import com.adidas.mvi.sample.app.di.AppModule
import com.adidas.mvi.sample.login.di.LoginModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

public class MviSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MviSampleApplication)
            modules(
                AppModule().module,
                LoginModule().module
            )
        }
    }
}