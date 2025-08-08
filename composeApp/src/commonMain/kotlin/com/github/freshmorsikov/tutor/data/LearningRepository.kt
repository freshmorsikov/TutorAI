package com.github.freshmorsikov.tutor.data

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class LearningRepository {

    private var currentNode: Node.Explored? = null

    fun exploreNode(
        id: String,
        topic: String,
        subtopics: List<String>,
        overview: String,
    ): Node.Explored {
        val newNode = createNode(
            id = id,
            topic = topic,
            subtopics = subtopics,
            overview = overview,
            parent = currentNode,
        )
        currentNode = newNode

        return newNode
    }

    fun switchToParent(topicId: String): Node.Explored? {
        var parent = currentNode?.parent
        while (parent != null && parent.id != topicId) {
            parent = parent.parent
        }
        if (parent != null) {
            currentNode = parent
        }

        return parent
    }

    fun switchToExplored(id: String): Node.Explored? {
        val subtopic = currentNode?.subtopics?.find { subtopic ->
            subtopic.id == id
        }

        return if (subtopic is Node.Explored) {
            currentNode = subtopic
            subtopic
        } else {
            null
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun createNode(
        id: String,
        topic: String,
        subtopics: List<String>,
        overview: String,
        parent: Node.Explored? = null,
    ): Node.Explored {
        val subtopicList: MutableList<Node> = mutableListOf()
        val node = Node.Explored(
            id = id,
            title = topic,
            parent = parent,
            overview = overview,
            subtopics = subtopicList
        )
        parent?.updateParent(node)
        subtopics.forEach { subtopic ->
            subtopicList.add(
                Node.Unexplored(
                    id = Uuid.random().toString(),
                    title = subtopic,
                    parent = node,
                )
            )
        }

        return node
    }

    private fun Node.Explored.updateParent(node: Node.Explored) {
        val index = subtopics
            .indexOfFirst { subtopic ->
                subtopic.id == node.id
            }
            .takeIf { it != -1 } ?: return

        subtopics[index] = node
    }

}