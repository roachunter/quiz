package com.example.quiz.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quiz.domain.category.Category
import com.example.quiz.ui.theme.ColorSet
import kotlinx.coroutines.launch

/**
 * Shows a button to pick a category
 */
@Composable
fun CategoryButton(
    category: Category,
    colorSet: ColorSet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // set of fields to create 3d pressing effect
    val topLayerElevation = remember { 6.dp }
    val offset = remember {
        Animatable(
            initialValue = 0f
        )
    }
    val coroutineScope = rememberCoroutineScope()

    // main button container
    Box(modifier = modifier) {

        // dark part at the bottom
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = topLayerElevation)
                .align(Alignment.BottomCenter)
                .clip(MaterialTheme.shapes.large)
                .background(colorSet.dark)
        )

        // top clickable part with button content
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = topLayerElevation)
                .align(Alignment.TopCenter)
                .offset(y = offset.value.dp)
                .clip(MaterialTheme.shapes.large)
                .background(colorSet.main)
                .clickable {
                    coroutineScope.launch {
                        offset.animateTo(topLayerElevation.value)
                        offset.animateTo(0f)
                    }
                    onClick()
                }
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                tint = colorSet.light,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(100.dp)
                    .scale(2f)
                    .offset(x = 25.dp, y = 20.dp)
            )

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}