package com.example.serials

import android.app.Application
import com.example.serials.di.AppComponent
import com.example.serials.di.AppModule
import com.example.serials.di.DaggerAppComponent

class SerialsApp: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}