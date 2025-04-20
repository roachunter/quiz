package com.example.quiz.presentation

sealed interface QuizRoute {
    data object Home: QuizRoute
    data object Quiz: QuizRoute
    data object Results: QuizRoute
}