package com.example.quiz.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quiz.ui.theme.ColorSet

/**
 * Displays 3d buttons from which only one can be selected
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizSegmentedButtons(
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    colorSet: ColorSet,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        labels.forEachIndexed { index, label ->
            QuizSegmentedButton(
                label = label,
                onClick = {
                    onSelect(index)
                },
                isSelected = selectedIndex == index,
                colorSet = colorSet,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun QuizSegmentedButton(
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    colorSet: ColorSet,
    modifier: Modifier = Modifier
) {
    // set of fields to create 3d pressing effect
    val topLayerElevation = remember { 4.dp }
    val offset by animateDpAsState(
        targetValue = if (isSelected) topLayerElevation else 0.dp
    )

    // main button container
    Box(modifier.wrapContentSize()) {

        // dark part at the bottom
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = topLayerElevation)
                .align(Alignment.BottomCenter)
                .background(
                    animateColorAsState(
                        targetValue = if (isSelected) {
                            colorSet.dark
                        } else MaterialTheme.colorScheme.outlineVariant
                    ).value,
                    MaterialTheme.shapes.small
                )
        )

        // top clickable part with button content
        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .wrapContentWidth()
                .padding(bottom = topLayerElevation)
                .align(Alignment.TopCenter)
                .offset(y = offset.value.dp)
                .clip(MaterialTheme.shapes.small)
                .background(
                    animateColorAsState(
                        targetValue = if (isSelected) {
                            colorSet.main
                        } else MaterialTheme.colorScheme.surfaceVariant
                    ).value
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}