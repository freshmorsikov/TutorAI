package com.github.freshmorsikov.tutor.presentation

sealed interface MainState {

    data class WaitingForTopic(
        val input: String = "",
        val isLoading: Boolean = false,
    ) : MainState

    data class Data(
        val topicChain: List<String>,
        val topic: String,
        val overview: String,
        val subtopics: List<Subtopic>,
    ) : MainState {

        val topics: String
            get() = topicChain.joinToString(" > ")

        val isBackEnabled: Boolean
            get() = topicChain.size > 1

    }

    data class Subtopic(
        val id: String,
        val title: String,
        val isLoading: Boolean,
        val isExplored: Boolean,
    )

}