package com.example.quiz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quiz.R
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.presentation.QuizEvent
import com.example.quiz.presentation.QuizState
import com.example.quiz.ui.theme.ColorSets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSetupDialog(
    state: QuizState,
    onEvent: (QuizEvent) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 28.dp)
        ) {
            Text(
                text = stringResource(R.string.set_up_your_quiz),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = state.pickedCategory.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            var amount by remember {
                mutableFloatStateOf(state.pickedQuestionAmount.toFloat())
            }
            Text(
                text = stringResource(R.string.question_amount)
                        + "   ${amount.toInt()}"
            )

            Spacer(Modifier.height(8.dp))

            QuizSlider(
                value = amount,
                onValueChange = {
                    amount = it
                },
                onValueChangeFinished = {
                    onEvent(QuizEvent.OnQuestionAmountPick(amount.toInt()))
                },
                colorSet = ColorSets.red
            )

            Spacer(Modifier.height(16.dp))

            Text(text = stringResource(R.string.difficulty))

            Spacer(Modifier.height(8.dp))

            val difficulties = mapOf(
                QuestionDifficulty.Easy to stringResource(R.string.easy),
                QuestionDifficulty.Medium to stringResource(R.string.medium),
                QuestionDifficulty.Hard to stringResource(R.string.hard),
            )
            QuizSegmentedButtons(
                labels = difficulties.values.toList(),
                selectedIndex = difficulties.keys.toList().indexOf(state.pickedDifficulty),
                onSelect = {
                    onEvent(QuizEvent.OnQuestionDifficultyPick(difficulties.keys.toList()[it]))
                },
                colorSet = ColorSets.red,
            )

            Spacer(Modifier.height(16.dp))

            Text(text = stringResource(R.string.type))

            Spacer(Modifier.height(8.dp))

            val types = mapOf(
                QuestionType.Any to stringResource(R.string.any),
                QuestionType.Multiple to stringResource(R.string.multiple_choise),
                QuestionType.Boolean to stringResource(R.string.true_false),
            )
            QuizSegmentedButtons(
                labels = types.values.toList(),
                selectedIndex = types.keys.toList().indexOf(state.pickedType),
                onSelect = {
                    onEvent(QuizEvent.OnQuestionTypePick(types.keys.toList()[it]))
                },
                colorSet = ColorSets.red,
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(text = stringResource(R.string.cancel))
                }

                Button(
                    onClick = {
                        //TODO start quiz
                    }
                ) {
                    Text(text = stringResource(R.string.start_quiz))
                }
            }
        }
    }
}