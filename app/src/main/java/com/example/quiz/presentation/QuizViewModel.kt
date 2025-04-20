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
import com.example.quiz.domain.session.QuizSession
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

    private val pickedAnswers = mutableListOf<String>()

    init {
        loadCategories()
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            QuizEvent.OnLoadCategoriesClick -> loadCategories()

            is QuizEvent.OnCategorySelected -> updatePickedCategory(event.category)
            is QuizEvent.OnQuestionAmountPick -> updatePickedQuestionAmount(event.amount)
            is QuizEvent.OnQuestionDifficultyPick -> updatePickedDifficulty(event.difficulty)
            is QuizEvent.OnQuestionTypePick -> updatePickedType(event.type)

            QuizEvent.OnStartQuizClick -> startQuiz()
            is QuizEvent.OnAnswerPicked -> processAnswer(event.answer)
            QuizEvent.OnGoHomeClick -> goHome()
        }
    }

    private fun goHome() {
        _state.update {
            it.copy(
                currentRoute = QuizRoute.Home,
                questions = emptyList()
            )
        }
    }

    private fun finishQuiz() {
        val quizSession = QuizSession(
            category = _state.value.pickedCategory,
            questions = _state.value.questions,
            pickedAnswers = pickedAnswers.toList()
        )

        pickedAnswers.clear()

        _state.update {
            it.copy(
                currentRoute = QuizRoute.Results,
                quizSession = quizSession
            )
        }
    }

    private fun processAnswer(answer: String) {
        pickedAnswers += answer
        if (pickedAnswers.size == _state.value.questions.size) {
            finishQuiz()
            return
        }

        _state.update {
            it.copy(
                currentQuestionNumber = it.currentQuestionNumber + 1
            )
        }
    }

    private fun startQuiz() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingQuestions = true
                )
            }

            val category = _state.value.pickedCategory
            val amount = _state.value.pickedQuestionAmount
            val difficulty = _state.value.pickedDifficulty
            val type = _state.value.pickedType

            val quiz = getQuestions(
                amount = amount,
                category = category,
                difficulty = difficulty.takeIf { it != QuestionDifficulty.Any },
                type = type.takeIf { it != QuestionType.Any }
            )

            /*val quiz = List(amount) {
                Question(
                    type = type,
                    difficulty = difficulty,
                    category = category.name,
                    questionText = "Test question #$it",
                    correctAnswer = "Correct answer",
                    incorrectAnswers = List(3) {
                        "Incorrect answer #$it"
                    }
                )
            }*/

            _state.update {
                it.copy(
                    isLoadingQuestions = false
                )
            }

            quiz?.let { questions ->
                _state.update {
                    it.copy(
                        currentRoute = QuizRoute.Quiz,
                        questions = questions,
                        currentQuestionNumber = 0
                    )
                }
            }
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

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoadingCategories = true)
            }

            val categories = getCategories()

            _state.update {
                it.copy(
                    categories = categories ?: it.categories,
                    /*categories = List(30) { index ->
                        Category(
                            id = index, name = "Test #$index", group = when (index) {
                                in 0..9 -> "Test category 1"
                                in 10..19 -> "Test category 2"
                                else -> "Test category 3"
                            }
                        )
                    },*/
                    isLoadingCategories = false
                )
            }
        }
    }

    private suspend fun getCategories(): List<Category>? {
        return when (val result = quizProvider.getCategories()) {
            is Result.Success -> result.data
            is Result.Error -> {
                handleFetchingError(result.error)
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
                handleFetchingError(result.error)
                null
            }
        }
    }

    private suspend fun handleFetchingError(error: DataError) {
        val errorMessage = when (error) {
            DataError.Network.NoInternet -> R.string.no_internet_error
            DataError.Network.RequestTimeout -> R.string.request_timeout_error

            DataError.Network.InvalidDataFormat -> R.string.invalid_data_format_error
            DataError.Network.NoResults -> R.string.no_results_for_quiz_config_error
            DataError.Network.RateLimit -> R.string.rate_limit_error

            else -> R.string.unknown_error
        }

        MessageController.sendEvent(MessageEvent(errorMessage))
    }
}