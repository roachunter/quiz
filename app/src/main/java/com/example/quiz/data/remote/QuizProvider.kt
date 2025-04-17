package com.example.quiz.data.remote

import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.Question
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.domain.result.DataError
import com.example.quiz.domain.result.Result

interface QuizProvider {
    suspend fun getQuiz(
        questionAmount: Int,
        category: Category? = null,
        difficulty: QuestionDifficulty? = null,
        type: QuestionType? = null
    ): Result<List<Question>, DataError.Network>

    suspend fun getCategories(): Result<List<Category>, DataError.Network>
}