package com.example.quiz.presentation

/**
 * App destinations
 */
sealed interface QuizRoute {
    data object Home: QuizRoute
    data object Quiz: QuizRoute
    data object Results: QuizRoute
}