package com.example.quiz.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.quiz.domain.question.Question

@Composable
fun QuizScreen(
    state: QuizState,
    onEvent: (QuizEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val quizProgress by animateFloatAsState(
            targetValue = state.currentQuestionNumber.toFloat()
                    / state.pickedQuestionAmount,
        )

        LinearProgressIndicator(
            progress = { quizProgress },
            drawStopIndicator = {},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        AnimatedContent(
            targetState = state.currentQuestionNumber,
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) togetherWith slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { questionNumber ->
            val question by remember {
                derivedStateOf {
                    state.questions[questionNumber]
                }
            }

            QuestionContainer(
                question = question,
                onAnswerPicked = {
                    onEvent(QuizEvent.OnAnswerPicked(it))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun QuestionContainer(
    question: Question,
    onAnswerPicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 5.dp,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.extraLarge
                )
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question.questionText
            )
        }

        val shuffledAnswers = rememberSaveable {
            question.getShuffledAnswers()
        }

        shuffledAnswers.forEach { answer ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.extraLarge
                    )
                    .clickable {
                        onAnswerPicked(answer)
                    }
                    .padding(28.dp)
            ) {
                Text(
                    text = answer
                )
            }
        }
    }
}