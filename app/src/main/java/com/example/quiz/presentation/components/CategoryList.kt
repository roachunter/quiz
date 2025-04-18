package com.example.quiz.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quiz.domain.category.Category
import com.example.quiz.ui.theme.ColorSets

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryList(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedCategories = remember(categories) {
        categories.groupBy {
            it.group
        }
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        groupedCategories.forEach { (group, categories) ->
            stickyHeader(key = group) {
                Text(
                    text = group,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(
                items = categories.chunked(2),
                key = { it.toString() }
            ) { categoriesPair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoriesPair.forEach { category ->
                        CategoryButton(
                            category = category,
                            colorSet = ColorSets.random,
                            onClick = { onCategoryClick(category) },
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                        )
                    }
                }
            }
        }


        item {
            Spacer(Modifier.height(200.dp))
        }
    }
}