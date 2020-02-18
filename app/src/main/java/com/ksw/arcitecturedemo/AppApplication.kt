package com.ksw.arcitecturedemo

import android.app.Application
import com.ksw.presentation.di.viewModelModule
import com.ksw.cache.di.cacheModule
import com.ksw.data.di.dataModule
import com.ksw.domain.di.domainModule
import com.ksw.remote.firestore.di.fireBaseRemoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class AppApplication :Application(){

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@AppApplication)
            modules(listOf(domainModule, dataModule, cacheModule, fireBaseRemoteModule,viewModelModule))
            logger(AndroidLogger(Level.DEBUG))
        }
    }
}