package com.example.todoapp

import android.app.Application
import com.example.todoapp.di.databaseModule
import com.example.todoapp.di.factoryModule
import com.example.todoapp.di.repositoryModule
import com.example.todoapp.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(viewModule, factoryModule, databaseModule, repositoryModule))
        }
    }
}