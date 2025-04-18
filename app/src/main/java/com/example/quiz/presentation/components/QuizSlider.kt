package com.example.quiz.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quiz.ui.theme.ColorSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    colorSet: ColorSet,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Slider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = 5f..20f,
        interactionSource = interactionSource,
        steps = 2,
        thumb = {
            val topLayerElevation = remember { 4.dp }
            val isDragged by interactionSource.collectIsDraggedAsState()
            val offset by animateDpAsState(
                targetValue = if (!isDragged) 0.dp else topLayerElevation
            )

            Box(modifier = Modifier.size(28.dp)) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = topLayerElevation)
                        .align(Alignment.BottomCenter)
                        .background(colorSet.dark, MaterialTheme.shapes.small)
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(bottom = topLayerElevation)
                        .align(Alignment.TopCenter)
                        .offset(y = offset)
                        .background(colorSet.main, MaterialTheme.shapes.small)
                )
            }
        },
        track = { sliderState ->
            val fraction by remember {
                derivedStateOf {
                    (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                }
            }

            Box(Modifier.fillMaxWidth().offset(y = 2.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .align(Alignment.CenterStart)
                        .height(8.dp)
                        .background(colorSet.light, MaterialTheme.shapes.small)
                )

                Box(
                    Modifier
                        .fillMaxWidth(1f - fraction)
                        .align(Alignment.CenterEnd)
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.small)
                )
            }
        },
        modifier = modifier
    )
}