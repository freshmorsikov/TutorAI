package com.github.freshmorsikov.tutor.data

import com.github.freshmorsikov.tutor.domain.Topic

class LearningRepository {

    private var currentNode: Node.Explored? = null

    fun exploreTopic(topic: Topic) {
        val node = createNode(
            topic = topic,
            parent = currentNode,
        )
        currentNode = node
    }

    fun switchToParent(topicId: String): Topic? {
        while (currentNode != null && currentNode?.id != topicId) {
            currentNode = currentNode?.parent?.updateExploredSubtopics()
        }

        return currentNode?.topic
    }

    fun switchToExplored(id: String): Topic? {
        val childNode = currentNode?.subtopics?.find { subtopic ->
            subtopic.id == id
        }

        return if (childNode is Node.Explored) {
            currentNode = childNode.updateExploredSubtopics()
            currentNode?.topic
        } else {
            null
        }
    }

    private fun Node.Explored.updateExploredSubtopics(): Node.Explored {
        return copy(
            topic = topic.copy(
                subtopics = topic.subtopics.map { subtopic ->
                    val isExplored = subtopics.any { nodeSubtopic ->
                        nodeSubtopic.id == subtopic.id && nodeSubtopic is Node.Explored
                    }
                    subtopic.copy(isExplored = isExplored)
                }
            )
        )
    }

    private fun createNode(
        topic: Topic,
        parent: Node.Explored? = null,
    ): Node.Explored {
        val subtopicList: MutableList<Node> = mutableListOf()
        val node = Node.Explored(
            parent = parent,
            topic = topic,
            subtopics = subtopicList
        )
        parent?.updateParent(node)
        topic.subtopics.forEach { subtopic ->
            subtopicList.add(
                Node.Unexplored(
                    id = subtopic.id,
                    title = subtopic.title,
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