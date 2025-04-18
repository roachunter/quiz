package com.example.quiz.di

import com.example.quiz.data.remote.OpenTDBQuizProvider
import com.example.quiz.data.remote.QuizProvider
import com.example.quiz.presentation.QuizViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    single<QuizProvider> {
        OpenTDBQuizProvider(
            client = get(),
            context = androidContext()
        )
    }

    viewModelOf(::QuizViewModel)
}