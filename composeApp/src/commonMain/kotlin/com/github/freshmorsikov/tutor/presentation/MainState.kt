package com.github.freshmorsikov.tutor.presentation

sealed interface MainState {

    data class WaitingForTopic(
        val input: String = "",
        val isLoading: Boolean = false,
    ) : MainState

    data class Data(
        val topicChain: List<String>,
        val currentLearningNode: LearningNode.Explored,
        val loadingSubtopicId: String?,
    ) : MainState {

        val topics: String
            get() = topicChain.joinToString(" > ")

        val isBackEnabled: Boolean
            get() = topicChain.size > 1

    }

}