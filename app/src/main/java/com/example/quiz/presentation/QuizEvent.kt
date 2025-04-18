package com.example.quiz.presentation

import com.example.quiz.domain.category.Category

sealed interface QuizEvent {
    data object OnLoadCategoriesClick: QuizEvent
    data class OnCategorySelected(val category: Category): QuizEvent
}