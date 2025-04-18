package com.example.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.R
import com.example.quiz.data.remote.QuizProvider
import com.example.quiz.domain.category.Category
import com.example.quiz.domain.question.Question
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.domain.result.DataError
import com.example.quiz.domain.result.Result
import com.example.quiz.presentation.message.MessageController
import com.example.quiz.presentation.message.MessageEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val quizProvider: QuizProvider
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = QuizState()
    )

    init {
        updateCategories()
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            QuizEvent.OnLoadCategoriesClick -> updateCategories()

            is QuizEvent.OnCategorySelected -> updatePickedCategory(event.category)
            is QuizEvent.OnQuestionAmountPick -> updatePickedQuestionAmount(event.amount)
            is QuizEvent.OnQuestionDifficultyPick -> updatePickedDifficulty(event.difficulty)
            is QuizEvent.OnQuestionTypePick -> updatePickedType(event.type)
        }
    }

    private fun updatePickedType(type: QuestionType) {
        _state.update {
            it.copy(
                pickedType = type
            )
        }
    }

    private fun updatePickedDifficulty(difficulty: QuestionDifficulty) {
        _state.update {
            it.copy(
                pickedDifficulty = difficulty
            )
        }
    }

    private fun updatePickedQuestionAmount(amount: Int) {
        _state.update {
            it.copy(
                pickedQuestionAmount = amount
            )
        }
    }

    private fun updatePickedCategory(category: Category) {
        _state.update {
            it.copy(
                pickedCategory = category
            )
        }
    }

    private fun updateCategories() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            //val categories = getCategories()

            _state.update {
                it.copy(
                    //categories = categories ?: it.categories,
                    categories = List(30) { index ->
                        Category(
                            id = index, name = "Test #$index", group = when (index) {
                                in 0..9 -> "Test category 1"
                                in 10..19 -> "Test category 2"
                                else -> "Test category 3"
                            }
                        )
                    },
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getCategories(): List<Category>? {
        return when (val result = quizProvider.getCategories()) {
            is Result.Success -> result.data
            is Result.Error -> {
                val errorMessage = when (result.error) {
                    DataError.Network.NoInternet -> R.string.no_internet_error
                    DataError.Network.RequestTimeout -> R.string.request_timeout_error

                    else -> R.string.unknown_error
                }

                MessageController.sendEvent(MessageEvent(errorMessage))

                null
            }
        }
    }

    private suspend fun getQuestions(
        amount: Int,
        category: Category? = null,
        difficulty: QuestionDifficulty? = null,
        type: QuestionType? = null
    ): List<Question>? {
        return when (val result = quizProvider.getQuiz(amount, category, difficulty, type)) {
            is Result.Success -> result.data
            is Result.Error -> {
                val errorMessage = when (result.error) {
                    DataError.Network.NoInternet -> R.string.no_internet_error
                    DataError.Network.RequestTimeout -> R.string.request_timeout_error

                    DataError.Network.InvalidDataFormat -> R.string.invalid_data_format_error
                    DataError.Network.NoResults -> R.string.no_results_for_quiz_config_error
                    DataError.Network.RateLimit -> R.string.rate_limit_error

                    else -> R.string.unknown_error
                }

                MessageController.sendEvent(MessageEvent(errorMessage))

                null
            }
        }
    }
}