package com.jay.typicodeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null
        fun getApplicationContext(): MainApplication {
            return instance as MainApplication
        }
    }
}
