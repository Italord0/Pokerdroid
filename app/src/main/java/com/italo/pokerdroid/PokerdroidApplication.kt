package com.italo.pokerdroid

import android.app.Application
import com.italo.pokerdroid.data.Preferences
import com.italo.pokerdroid.di.commonModule
import com.italo.pokerdroid.di.mainRepositoryModule
import com.italo.pokerdroid.di.mainViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class PokerdroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
        startKoin {
            androidContext(this@PokerdroidApplication)
            loadKoinModules(
                commonModule +
                        mainViewModelModule +
                        mainRepositoryModule
            )
        }
    }
}