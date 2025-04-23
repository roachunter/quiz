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

/**
 * Main Koin DI Module
 */
val appModule = module {

    // HttpClient instance
    single<HttpClient> {
        HttpClient(CIO) {
            // installing ContentNegotiation plugin for
            // to/from Json conversion
            install(ContentNegotiation) {
                json()
            }
        }
    }

    // QuizProvider instance to which our
    // Open Trivia Database implementation is bound
    single<QuizProvider> {
        OpenTDBQuizProvider(
            client = get(),
            context = androidContext()
        )
    }

    // QuizViewModel instance
    viewModelOf(::QuizViewModel)
}