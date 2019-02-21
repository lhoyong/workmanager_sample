package com.github.lhoyong.workmanager

import android.app.Application
import com.facebook.stetho.Stetho

class Application : Application() {


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}