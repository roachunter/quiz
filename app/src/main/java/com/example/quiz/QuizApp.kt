package com.example.quiz

import android.app.Application
import com.example.quiz.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuizApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QuizApp)
            modules(appModule)
        }
    }
}