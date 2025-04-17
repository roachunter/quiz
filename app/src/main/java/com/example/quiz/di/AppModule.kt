package com.example.quiz.di

import com.example.quiz.data.remote.OpenTDBQuizProvider
import com.example.quiz.data.remote.QuizProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation)
        }
    }

    single<QuizProvider> {
        OpenTDBQuizProvider(client = get())
    }
}