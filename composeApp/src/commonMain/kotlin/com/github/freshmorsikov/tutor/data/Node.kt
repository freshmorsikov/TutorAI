package com.github.freshmorsikov.tutor.data

import com.github.freshmorsikov.tutor.domain.Topic

sealed interface Node {

    val id: String
    val title: String
    val parent: Node?

    data class Explored(
        override val parent: Explored?,
        val topic: Topic,
        val subtopics: MutableList<Node>,
    ) : Node {

        override val id: String
            get() = topic.id

        override val title: String
            get() = topic.title
    }

    data class Unexplored(
        override val id: String,
        override val title: String,
        override val parent: Explored?,
    ) : Node

}