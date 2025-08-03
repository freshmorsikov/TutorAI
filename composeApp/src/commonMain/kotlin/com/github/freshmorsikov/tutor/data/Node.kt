package com.github.freshmorsikov.tutor.data

sealed interface Node {

    val id: String
    val title: String
    val parent: Node?

    data class Explored(
        override val id: String,
        override val title: String,
        override val parent: Explored?,
        val overview: String,
        val subtopics: MutableList<Node>,
    ) : Node

    data class Unexplored(
        override val id: String,
        override val title: String,
        override val parent: Explored?,
    ) : Node

}