package com.example.quiz.presentation

import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.Question
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.domain.session.QuizSession

/**
 * App and UI state
 */
data class QuizState(
    // screen to show to user
    val currentRoute: QuizRoute = QuizRoute.Home,

    val isLoadingCategories: Boolean = false,
    val isLoadingQuestions: Boolean = false,

    // list of fetched categories
    val categories: List<Category> = emptyList(),

    // quiz customization parameters
    val pickedCategory: Category = Category(-1, "", ""),
    val pickedQuestionAmount: Int = 10,
    val pickedDifficulty: QuestionDifficulty = QuestionDifficulty.Any,
    val pickedType: QuestionType = QuestionType.Any,

    // list of fetched questions
    val questions: List<Question> = emptyList(),
    val currentQuestionNumber: Int = 0,

    // quiz result
    val quizSession: QuizSession = QuizSession(
        category = pickedCategory,
        questions = emptyList(),
        pickedAnswers = emptyList()
    )
)
