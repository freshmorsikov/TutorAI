package com.github.freshmorsikov.tutor.presentation

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface LearningNode {
    val id: String
    val topic: String
    val parent: LearningNode?

    data class Explored(
        override val id: String,
        override val topic: String,
        override val parent: Explored?,
        val overview: String,
        val subtopics: List<LearningNode>,
    ) : LearningNode

    data class Unexplored(
        override val id: String,
        override val topic: String,
        override val parent: Explored?,
    ) : LearningNode

}

@OptIn(ExperimentalUuidApi::class)
fun createLearningNode(
    id: String,
    topic: String,
    subtopics: List<String>,
    overview: String,
    parent: LearningNode.Explored? = null
): LearningNode.Explored {
    val node = LearningNode.Explored(
        id = id,
        topic = topic,
        parent = parent,
        overview = overview,
        subtopics = emptyList()
    )
    val subtopics = subtopics.map { subtopic ->
        LearningNode.Unexplored(
            id = Uuid.random().toString(),
            topic = subtopic,
            parent = node,
        )
    }
    return node.copy(subtopics = subtopics)
}