package com.example.quiz.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quiz.R
import com.example.quiz.presentation.components.CategoryList
import com.example.quiz.presentation.components.QuizSetupDialog
import com.example.quiz.ui.theme.ColorSets

@Composable
fun HomeScreen(
    state: QuizState,
    onEvent: (QuizEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 28.dp, top = 16.dp)
        )

        var showQuizSetupDialog by remember {
            mutableStateOf(false)
        }
        AnimatedContent(
            targetState = state.isLoadingCategories
        ) { isLoading ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                when (isLoading) {
                    true -> {
                        Text(text = stringResource(R.string.looking_for_categories))

                        LinearProgressIndicator(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(top = 8.dp)
                        )
                    }
                    false -> {
                        CategoryList(
                            categories = state.categories,
                            onCategoryClick = {
                                onEvent(QuizEvent.OnCategorySelected(it))
                                showQuizSetupDialog = true
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        if (showQuizSetupDialog) {
            QuizSetupDialog(
                state = state,
                colorSet = ColorSets.red,
                onEvent = onEvent,
                onDismissRequest = {
                    showQuizSetupDialog = false
                }
            )
        }
    }
}