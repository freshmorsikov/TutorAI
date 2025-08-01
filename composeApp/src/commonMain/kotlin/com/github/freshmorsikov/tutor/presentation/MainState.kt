package com.github.freshmorsikov.tutor.presentation

sealed interface MainState {

    data class WaitingForTopic(
        val input: String = "",
        val isLoading: Boolean = false,
    ) : MainState

    data class Data(
        val currentLearningNode: LearningNode.Explored,
        val loadingSubtopicId: String?
    ) : MainState

}