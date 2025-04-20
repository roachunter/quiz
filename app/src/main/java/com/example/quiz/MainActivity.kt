package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.quiz.presentation.HomeScreen
import com.example.quiz.presentation.QuizResultsScreen
import com.example.quiz.presentation.QuizRoute
import com.example.quiz.presentation.QuizScreen
import com.example.quiz.presentation.QuizViewModel
import com.example.quiz.presentation.message.ScaffoldWithMessageEvents
import com.example.quiz.ui.theme.QuizTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizTheme {
                ScaffoldWithMessageEvents(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {

                        val viewModel = getViewModel<QuizViewModel>()
                        val state by viewModel.state.collectAsState()

                        AnimatedContent(
                            targetState = state.currentRoute
                        ) { route ->
                            when (route) {
                                QuizRoute.Home -> {
                                    HomeScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }
                                QuizRoute.Quiz -> {
                                    QuizScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }
                                QuizRoute.Results -> {
                                    QuizResultsScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
