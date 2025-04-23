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
                // custom scaffold that allows message box displaying
                ScaffoldWithMessageEvents(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    // main app container
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {

                        // getting view model from our koin appModule
                        val viewModel = getViewModel<QuizViewModel>()
                        // getting ui state from view model
                        val state by viewModel.state.collectAsState()

                        // animating transition between different screens
                        AnimatedContent(
                            targetState = state.currentRoute
                        ) { route ->
                            when (route) {
                                // route with the list of categories
                                QuizRoute.Home -> {
                                    HomeScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }

                                // route for quiz questions
                                QuizRoute.Quiz -> {
                                    QuizScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }

                                // route for quiz results
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
