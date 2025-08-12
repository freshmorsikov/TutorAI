package com.github.freshmorsikov.tutor.data

import com.github.freshmorsikov.tutor.domain.Topic

class LearningRepository {

    private var currentNode: Node.Explored? = null

    fun exploreNode(topic: Topic): Node.Explored {
        val newNode = createNode(
            topic = topic,
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