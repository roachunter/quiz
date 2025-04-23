package com.example.quiz.presentation

import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType

/**
 * Events that can happen in UI
 */
sealed interface QuizEvent {
    data object OnLoadCategoriesClick : QuizEvent

    data class OnCategorySelected(val category: Category) : QuizEvent
    data class OnQuestionAmountPick(val amount: Int) : QuizEvent
    data class OnQuestionDifficultyPick(val difficulty: QuestionDifficulty) : QuizEvent
    data class OnQuestionTypePick(val type: QuestionType) : QuizEvent

    data object OnStartQuizClick: QuizEvent
    data class OnAnswerPicked(val answer: String): QuizEvent
    data object OnGoHomeClick: QuizEvent
}