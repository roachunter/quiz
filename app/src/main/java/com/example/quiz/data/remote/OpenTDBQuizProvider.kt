package com.example.quiz.data.remote

import android.content.Context
import android.util.Log
import com.example.quiz.R
import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.Question
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.domain.result.DataError
import com.example.quiz.domain.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.SocketException
import java.net.URLDecoder

/**
 * [QuizProvider] implementation for Open Trivia Database API
 */
class OpenTDBQuizProvider(
    private val context: Context,
    private val client: HttpClient
) : QuizProvider {
    private val logTag = "QuizProvider"

    override suspend fun getQuiz(
        questionAmount: Int,
        category: Category?,
        difficulty: QuestionDifficulty?,
        type: QuestionType?
    ): Result<List<Question>, DataError.Network> {
        val response = handleRequest {
            // requesting api for quiz
            client.get(OpenTDBApiConfig.QUIZ_ENDPOINT) {
                // specifying request parameters
                url {
                    parameters.append("amount", "$questionAmount")

                    // category is ignored if not specified
                    category?.let {
                        parameters.append("category", "${it.id}")
                    }

                    // difficulty is ignored if not specified
                    difficulty?.let {
                        val diff = when (it) {
                            QuestionDifficulty.Easy -> "easy"
                            QuestionDifficulty.Medium -> "medium"
                            QuestionDifficulty.Hard -> "hard"
                            else -> return@let
                        }
                        parameters.append("difficulty", diff)
                    }

                    // type is ignored if not specified
                    type?.let {
                        val t = when (it) {
                            QuestionType.Multiple -> "multiple"
                            QuestionType.Boolean -> "boolean"
                            else -> return@let
                        }
                        parameters.append("type", t)
                    }

                    // encoding for easier answer decoding
                    parameters.append("encode", "url3986")
                }
            }
        }.let {
            // helper returns Result wrapper, so unwrapping it
            when (it) {
                is Result.Success -> it.data
                is Result.Error -> return Result.Error(it.error)
            }
        }

        // checking response code and returning result
        return when (response.status) {
            HttpStatusCode.OK -> {
                // getting response body as QuizDto
                val quiz = response.body<QuizDto>()

                // checking api response code
                when (quiz.responseCode) {
                    // means ok
                    0 -> {
                        // mapping questions to our model
                        val questions = try {
                            quiz.results.map { it.toModel() }
                        } catch (e: IllegalArgumentException) {
                            Log.w(logTag, e.message ?: "Invalid question format received.")
                            return Result.Error(DataError.Network.InvalidDataFormat)
                        }

                        Result.Success(questions)
                    }

                    // means no results found
                    1 -> Result.Error(DataError.Network.NoResults)

                    // means requests are too frequent (limit 1 request per 5 seconds)
                    5 -> Result.Error(DataError.Network.RateLimit)

                    // other codes are unexpected
                    else -> {
                        Log.w(logTag, "Unexpected api response code: ${quiz.responseCode}")
                        Result.Error(DataError.Network.Unknown)
                    }
                }
            }

            // other status codes are unexpected
            else -> {
                Log.w(logTag, "Unexpected quiz response status code: ${response.status}")
                Result.Error(DataError.Network.Unknown)
            }
        }
    }

    override suspend fun getCategories(): Result<List<Category>, DataError.Network> {
        val response = handleRequest {
            // requesting api for categories
            client.get(OpenTDBApiConfig.CATEGORY_ENDPOINT)
        }.let {
            // helper returns Result wrapper, so unwrapping it
            when (it) {
                is Result.Success -> it.data
                is Result.Error -> return Result.Error(it.error)
            }
        }

        // checking response code and returning result
        return when (response.status) {
            HttpStatusCode.OK -> {
                // getting response body as CategoriesDto
                val categoriesDto = response.body<CategoriesDto>()

                // mapping categories to out model
                val categories = categoriesDto.triviaCategories.map { it.toModel(context) }
                Result.Success(categories)
            }

            // other status codes are unexpected
            else -> {
                Log.w(logTag, "Unexpected categories response status code: ${response.status}")
                Result.Error(DataError.Network.Unknown)
            }
        }
    }

    /**
     * Helper method, that provides generic ktor request error handling
     *
     * @return [Result.Success] with [HttpResponse] as data if no errors occurred,
     * or [Result.Error] with a corresponding [DataError.Network]
     */
    private suspend fun handleRequest(request: suspend () -> HttpResponse): Result<HttpResponse, DataError.Network> {
        return try {
            Result.Success(request())
        } catch (_: UnresolvedAddressException) {
            Result.Error(DataError.Network.NoInternet)
        } catch (_: HttpRequestTimeoutException) {
            Result.Error(DataError.Network.RequestTimeout)
        } catch (e: SocketException) {
            Log.e(logTag, e.message ?: "Socket Exception thrown")
            e.printStackTrace()
            Result.Error(DataError.Network.Unknown)
        } catch (e: Exception) {
            Log.e(logTag, e.message ?: "Exception thrown")
            e.printStackTrace()
            Result.Error(DataError.Network.Unknown)
        } catch (e: java.lang.Exception) {
            Log.e(logTag, e.message ?: "java.lang.Exception thrown")
            e.printStackTrace()
            Result.Error(DataError.Network.Unknown)
        }
    }
}

@Serializable
private data class QuizDto(
    @SerialName("response_code")
    val responseCode: Int,
    val results: List<QuestionDto>
)

@Serializable
private data class QuestionDto(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    @SerialName("correct_answer")
    val correctAnswer: String,
    @SerialName("incorrect_answers")
    val incorrectAnswers: List<String>
)

private fun QuestionDto.toModel(): Question =
    Question(
        type = when (type) {
            "multiple" -> QuestionType.Multiple
            "boolean" -> QuestionType.Boolean
            else -> throw IllegalArgumentException("Unknown question type: $type")
        },
        difficulty = when (difficulty) {
            "easy" -> QuestionDifficulty.Easy
            "medium" -> QuestionDifficulty.Medium
            "hard" -> QuestionDifficulty.Hard
            else -> throw IllegalArgumentException("Unknown question difficulty: $difficulty")
        },
        category = decodeUrl(category),
        questionText = decodeUrl(question),
        correctAnswer = decodeUrl(correctAnswer),
        incorrectAnswers = incorrectAnswers.map {
            decodeUrl(it)
        }
    )

@Serializable
private data class CategoriesDto(
    @SerialName("trivia_categories")
    val triviaCategories: List<CategoryDto>
)

@Serializable
private data class CategoryDto(
    val id: Int,
    val name: String
)

private fun CategoryDto.toModel(context: Context): Category {
    val split = name.split(": ")

    val group = if (split.size > 1) {
        split.first()
    } else context.resources.getString(R.string.general)

    val categoryName = split.last()

    return Category(
        id = id,
        name = categoryName,
        group = group
    )
}

private fun decodeUrl(url: String) =
    URLDecoder.decode(url, "UTF-8")