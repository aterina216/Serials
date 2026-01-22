package com.example.serials.di

import com.example.serials.MainActivity
import com.example.serials.ui.viewmodel.SerialsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}