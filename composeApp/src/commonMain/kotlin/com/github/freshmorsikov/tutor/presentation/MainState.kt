package com.github.freshmorsikov.tutor.presentation

sealed interface MainState {
    data class WaitingForTopic(
        val input: String = "",
        val isLoading: Boolean = false,
    ) : MainState
    data class Data(
        val topic: String,
        val overview: String,
        val subtopics: List<String>,
    ) : MainState
}