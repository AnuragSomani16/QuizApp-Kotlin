package com.example.quizapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.quizapp.di.KoinModules
import com.example.quizapp.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Light
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Dark
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) // Follow system
        startKoin {
            androidContext(this@QuizApplication)
            modules(
                KoinModules.dataModules +
                KoinModules.domainModules +
                listOf(presentationModule)
            )
        }
    }
}