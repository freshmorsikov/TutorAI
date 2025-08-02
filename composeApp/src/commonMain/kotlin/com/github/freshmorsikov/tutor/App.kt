package com.github.freshmorsikov.tutor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.freshmorsikov.tutor.icon.ArrowLeft
import com.github.freshmorsikov.tutor.icon.ArrowUp
import com.github.freshmorsikov.tutor.presentation.MainState
import com.github.freshmorsikov.tutor.presentation.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: MainViewModel = viewModel { MainViewModel() }) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        ) {
            val state by viewModel.state.collectAsState()
            val stateValue = state

            if (stateValue is MainState.Data) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val borderAlpha = if (stateValue.isBackEnabled) 1f else 0.4f
                    IconButton(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha),
                                shape = CircleShape
                            ),
                        enabled = stateValue.isBackEnabled,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f),
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        ),
                        onClick = {
                            viewModel.goToPreviousTopic()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.ArrowLeft,
                            contentDescription = null
                        )
                    }
                    Topics(topic = stateValue.topics)
                }
            }

            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (stateValue) {
                    is MainState.WaitingForTopic -> {
                        WaitingForTopic(
                            state = stateValue,
                            onValueChange = { input ->
                                viewModel.updateInput(input = input)
                            },
                            onStart = {
                                viewModel.start()
                            },
                        )
                    }

                    is MainState.Data -> {
                        Messages(
                            state = stateValue,
                            onSubtopicClick = { subtopicId ->
                                viewModel.selectSubtopic(subtopicId = subtopicId)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WaitingForTopic(
    state: MainState.WaitingForTopic,
    onValueChange: (String) -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Let's learn something new!\nWhat topic are you interested in?",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
    }

    val shape = RoundedCornerShape(32.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = shape
            )
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            ),
        verticalAlignment = Alignment.Bottom,
    ) {
        TextField(
            modifier = Modifier
                .padding(4.dp)
                .heightIn(min = 48.dp)
                .weight(1f),
            value = state.input,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Ask any topic you want to start learning",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            onValueChange = { value ->
                if (value.contains('\n')) {
                    onStart()
                } else {
                    onValueChange(value)
                }
            },
            enabled = !state.isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
            enabled = !state.isLoading && state.input.isNotBlank(),
            onClick = {
                onStart()
            }
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.ArrowUp,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun Topics(
    topic: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = topic,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
    )
}

@Composable
private fun Messages(
    state: MainState.Data,
    onSubtopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        item(key = "overview") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                SelectionContainer {
                    Text(
                        modifier = Modifier.weight(
                            weight = 1f,
                            fill = false,
                        ).background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(20.dp)
                        ).padding(16.dp),
                        text = state.currentLearningNode.overview,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }

        state.currentLearningNode.subtopics.forEach { subtopic ->
            item(key = "subtopic_${subtopic.id}") {
                Button(
                    onClick = {
                        onSubtopicClick(subtopic.id)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            modifier = Modifier.alpha(alpha = if (state.loadingSubtopicId == subtopic.id) 0f else 1f),
                            text = subtopic.topic,
                            style = MaterialTheme.typography.labelMedium
                        )

                        if (state.loadingSubtopicId == subtopic.id) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onSecondary,
                            )
                        }
                    }
                }
            }
        }
    }
}