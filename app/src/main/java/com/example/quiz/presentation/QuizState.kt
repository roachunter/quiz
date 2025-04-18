package com.example.quiz.presentation

import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType

data class QuizState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val pickedCategory: Category = Category(-1, "", ""),
    val pickedQuestionAmount: Int = 10,
    val pickedDifficulty: QuestionDifficulty = QuestionDifficulty.Medium,
    val pickedType: QuestionType = QuestionType.Any
)
