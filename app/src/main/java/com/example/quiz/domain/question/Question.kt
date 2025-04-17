package com.example.quiz.domain.question

data class Question(
    val type: QuestionType,
    val difficulty: QuestionDifficulty,
    val category: String,
    val questionText: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
) {
    fun getShuffledAnswers(): List<String> = (incorrectAnswers + correctAnswer).shuffled()
}
