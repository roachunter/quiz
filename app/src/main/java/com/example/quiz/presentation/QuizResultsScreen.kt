package com.example.quiz.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.quiz.R
import com.example.quiz.domain.question.Question
import com.example.quiz.ui.theme.Lime
import com.example.quiz.ui.theme.Rose

/**
 * Shows quiz results
 */
@Composable
fun QuizResultsScreen(
    state: QuizState,
    onEvent: (QuizEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // header
        Text(
            text = stringResource(R.string.results),
            style = MaterialTheme.typography.displayLarge
        )

        val quizSession = state.quizSession
        // displaying quiz category
        Text(
            text = quizSession.category.name,
            style = MaterialTheme.typography.titleLarge
        )

        // getting amount of questions
        val amount = quizSession.questions.size
        // calculating amount of correct answers
        val correctAnswersCount = quizSession.questions
            .foldIndexed(0) { index, acc, question ->
                if (question.correctAnswer == quizSession.pickedAnswers[index]) {
                    acc + 1
                } else acc
            }
        // calculating correctness percentage
        val correctPercentage = (correctAnswersCount * 100) / amount

        // displaying correctness percentage
        Text(
            // building a string with custom styles for different parts
            text = buildAnnotatedString {
                // applying styles to numeric part
                withStyle(
                    MaterialTheme.typography.headlineLarge.toSpanStyle()
                        .copy(fontWeight = FontWeight.Bold)
                ) {
                    //appending percentage to the string
                    append(correctPercentage.toString())
                }

                // applying styles to '%' symbol
                withStyle(
                    MaterialTheme.typography.headlineSmall.toSpanStyle()
                ) {
                    // appending symbol to the string
                    append('%')
                }
            }
        )

        // go home button
        Button(
            onClick = {
                onEvent(QuizEvent.OnGoHomeClick)
            }
        ) {
            Text(text = stringResource(R.string.back_to_home))
        }

        Spacer(Modifier.height(28.dp))

        // header for the list of answers
        Text(
            text = stringResource(R.string.answers),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        // list of answers
        Answers(
            questions = quizSession.questions,
            pickedAnswers = quizSession.pickedAnswers,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Shows lazy column with picked answers
 */
@Composable
private fun Answers(
    questions: List<Question>,
    pickedAnswers: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = questions,
            key = { index, question -> "${index}-${question.questionText}" }
        ) { index, question ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(16.dp)
            ) {
                // question text
                Text(text = question.questionText)

                Spacer(modifier = Modifier.height(16.dp))

                // checking if the answer is correct
                val isCorrect = remember {
                    question.correctAnswer == pickedAnswers[index]
                }

                // picked answer container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isCorrect) {
                                Lime
                            } else Rose,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(16.dp)
                ) {
                    // 'your answer' label
                    Text(
                        text = stringResource(R.string.your_answer),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(8.dp))

                    // picked answer text
                    Text(
                        text = pickedAnswers[index],
                        color = Color.White
                    )
                }

                // displaying correct answer if the picked one is incorrect
                if (!isCorrect) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // correct answer container
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(16.dp)
                    ) {
                        // 'correct answer' label
                        Text(
                            text = stringResource(R.string.correct_answer),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        // correct answer text
                        Text(text = question.correctAnswer)
                    }
                }
            }
        }
    }
}