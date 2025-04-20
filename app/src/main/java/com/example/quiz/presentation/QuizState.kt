package com.example.quiz.presentation

import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.Question
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.domain.session.QuizSession

data class QuizState(
    val currentRoute: QuizRoute = QuizRoute.Home,

    val isLoadingCategories: Boolean = false,
    val isLoadingQuestions: Boolean = false,

    val categories: List<Category> = emptyList(),

    val pickedCategory: Category = Category(-1, "", ""),
    val pickedQuestionAmount: Int = 10,
    val pickedDifficulty: QuestionDifficulty = QuestionDifficulty.Any,
    val pickedType: QuestionType = QuestionType.Any,

    val questions: List<Question> = emptyList(),
    val currentQuestionNumber: Int = 0,

    val quizSession: QuizSession = QuizSession(
        category = pickedCategory,
        questions = emptyList(),
        pickedAnswers = emptyList()
    )
)
