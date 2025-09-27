package com.example.wbmonitor

import android.app.Application
import androidx.work.Configuration

class App : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }
}
