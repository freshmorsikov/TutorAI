package com.github.freshmorsikov.tutor.domain

data class Topic(
    val id: String,
    val title: String,
    val overview: String,
    val videos: List<Video>,
    val subtopics: List<Subtopic>,
)

data class Video(
    val title: String,
    val url: String,
)

data class Subtopic(
    val id: String,
    val title: String,
)