package com.example.quiz.domain.session

import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.Question

data class QuizSession(
    val category: Category,
    val questions: List<Question>,
    val pickedAnswers: List<String>
)
