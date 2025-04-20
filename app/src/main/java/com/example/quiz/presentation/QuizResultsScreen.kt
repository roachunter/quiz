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
        Text(
            text = stringResource(R.string.results),
            style = MaterialTheme.typography.displayLarge
        )

        val quizSession = state.quizSession
        Text(
            text = quizSession.category.name,
            style = MaterialTheme.typography.titleLarge
        )

        val amount = quizSession.questions.size
        val correctAnswersCount = quizSession.questions
            .foldIndexed(0) { index, acc, question ->
                if (question.correctAnswer == quizSession.pickedAnswers[index]) {
                    acc + 1
                } else acc
            }

        val correctPercentage = (correctAnswersCount * 100) / amount

        Text(
            text = buildAnnotatedString {
                withStyle(
                    MaterialTheme.typography.headlineLarge.toSpanStyle()
                        .copy(fontWeight = FontWeight.Bold)
                ) {
                    append(correctPercentage.toString())
                }

                withStyle(
                    MaterialTheme.typography.headlineSmall.toSpanStyle()
                ) {
                    append('%')
                }
            }
        )

        Button(
            onClick = {
                onEvent(QuizEvent.OnGoHomeClick)
            }
        ) {
            Text(text = stringResource(R.string.back_to_home))
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.answers),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        Answers(
            questions = quizSession.questions,
            pickedAnswers = quizSession.pickedAnswers,
            modifier = Modifier.fillMaxSize()
        )
    }
}

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
                Text(text = question.questionText)

                Spacer(modifier = Modifier.height(16.dp))

                val isCorrect = remember {
                    question.correctAnswer == pickedAnswers[index]
                }
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
                    Text(
                        text = stringResource(R.string.your_answer),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = pickedAnswers[index],
                        color = Color.White
                    )
                }

                if (!isCorrect) {
                    Spacer(modifier = Modifier.height(16.dp))

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
                        Text(
                            text = stringResource(R.string.correct_answer),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(text = question.correctAnswer)
                    }
                }
            }
        }
    }
}