package com.github.freshmorsikov.tutor.presentation

import com.github.freshmorsikov.tutor.domain.Topic

sealed interface MainState {

    data class WaitingForTopic(
        val input: String = "",
        val isLoading: Boolean = false,
    ) : MainState

    data class Data(
        val topicChain: List<TopicChainItem>,
        val topic: Topic,
        val subtopics: List<Subtopic>,
    ) : MainState {

        val previousTopicId: String?
            get() {
                return if (topicChain.size > 1) {
                     topicChain[topicChain.lastIndex - 1].id
                } else {
                    null
                }
            }

        val isBackEnabled: Boolean
            get() = topicChain.size > 1

    }

    data class TopicChainItem(
        val id: String,
        val title: String,
    )

    data class Subtopic(
        val id: String,
        val title: String,
        val isLoading: Boolean,
        val isExplored: Boolean,
    )

}