package com.example.quiz.data.remote

/**
 * Open Trivia Database API config, contains endpoints
 */
object OpenTDBApiConfig {
    private const val BASE_URL = "https://opentdb.com/"
    const val CATEGORY_ENDPOINT = BASE_URL + "api_category.php"
    const val QUIZ_ENDPOINT = BASE_URL + "api.php"
}