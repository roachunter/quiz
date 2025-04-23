package com.example.quiz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quiz.R
import com.example.quiz.domain.question.QuestionDifficulty
import com.example.quiz.domain.question.QuestionType
import com.example.quiz.presentation.QuizEvent
import com.example.quiz.presentation.QuizState
import com.example.quiz.ui.theme.ColorSet
import com.example.quiz.ui.theme.ColorSets

/**
 * Dialog for setting up quiz parameters
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSetupDialog(
    state: QuizState,
    colorSet: ColorSet,
    onEvent: (QuizEvent) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    // main dialog container that displays it on top of all other content
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 28.dp)
        ) {
            // showing loading indicator if fetching questions
            // or main dialog content otherwise
            when (state.isLoadingQuestions) {
                true -> {
                    Text(
                        text = stringResource(R.string.getting_quiz_ready),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    LinearProgressIndicator(
                        color = colorSet.dark,
                        trackColor = colorSet.light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 28.dp)
                            .padding(top = 8.dp)
                    )
                }

                false -> {
                    QuizSetupDialogContent(
                        state = state,
                        colorSet = colorSet,
                        onEvent = onEvent,
                        onDismissRequest = onDismissRequest
                    )
                }
            }
        }
    }
}

/**
 * Main content of quiz setup dialog
 */
@Composable
private fun ColumnScope.QuizSetupDialogContent(
    state: QuizState,
    colorSet: ColorSet,
    onEvent: (QuizEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    // header
    Text(
        text = stringResource(R.string.set_up_your_quiz),
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(Modifier.height(16.dp))

    // picked category text
    Text(
        text = state.pickedCategory.name,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(Modifier.height(16.dp))

    // holds question amount picked on slider
    var amount by remember {
        mutableFloatStateOf(state.pickedQuestionAmount.toFloat())
    }
    // label for slider, amount is specified next to it
    Text(
        text = stringResource(R.string.question_amount) + "   ${amount.toInt()}"
    )

    Spacer(Modifier.height(8.dp))

    // slider for amount
    QuizSlider(
        value = amount,
        onValueChange = {
            // while the user is dragging, updating only amount in UI
            amount = it
        },
        onValueChangeFinished = {
            // sending amount to view model when the user stops dragging
            onEvent(QuizEvent.OnQuestionAmountPick(amount.toInt()))
        },
        colorSet = colorSet
    )

    Spacer(Modifier.height(16.dp))

    // label for difficulty
    Text(text = stringResource(R.string.difficulty))

    Spacer(Modifier.height(8.dp))

    // mapping QuestionDifficulty to its string representation in UI
    val difficulties = mapOf(
        QuestionDifficulty.Any to stringResource(R.string.any),
        QuestionDifficulty.Easy to stringResource(R.string.easy),
        QuestionDifficulty.Medium to stringResource(R.string.medium),
        QuestionDifficulty.Hard to stringResource(R.string.hard),
    )
    // difficulty buttons
    QuizSegmentedButtons(
        labels = difficulties.values.toList(),
        selectedIndex = difficulties.keys.toList().indexOf(state.pickedDifficulty),
        onSelect = { selectedIndex ->
            // sending picked difficulty to view model
            onEvent(QuizEvent.OnQuestionDifficultyPick(difficulties.keys.toList()[selectedIndex]))
        },
        colorSet = colorSet,
    )

    Spacer(Modifier.height(16.dp))

    // label for type
    Text(text = stringResource(R.string.type))

    Spacer(Modifier.height(8.dp))

    // mapping QuestionType to its string representation in UI
    val types = mapOf(
        QuestionType.Any to stringResource(R.string.any),
        QuestionType.Multiple to stringResource(R.string.multiple_choise),
        QuestionType.Boolean to stringResource(R.string.true_false),
    )
    // type buttons
    QuizSegmentedButtons(
        labels = types.values.toList(),
        selectedIndex = types.keys.toList().indexOf(state.pickedType),
        onSelect = { selectedIndex ->
            // sending picked type to view model
            onEvent(QuizEvent.OnQuestionTypePick(types.keys.toList()[selectedIndex]))
        },
        colorSet = ColorSets.red,
    )

    Spacer(Modifier.height(16.dp))

    // buttons at the bottom
    Row(
        modifier = Modifier.align(Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // button for canceling dialog
        TextButton(
            onClick = onDismissRequest,
            colors = ButtonDefaults.textButtonColors(
                contentColor = colorSet.main
            )
        ) {
            Text(text = stringResource(R.string.cancel))
        }

        // button for starting quiz
        Button(
            onClick = {
                onEvent(QuizEvent.OnStartQuizClick)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorSet.main,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.start_quiz))
        }
    }
}