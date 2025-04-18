package com.example.quiz.presentation

import com.example.quiz.domain.category.Category

data class QuizState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
)
