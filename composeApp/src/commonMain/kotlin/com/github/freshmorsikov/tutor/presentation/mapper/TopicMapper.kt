package com.github.freshmorsikov.tutor.presentation.mapper

import com.github.freshmorsikov.tutor.agent.TopicLLM
import com.github.freshmorsikov.tutor.domain.Subtopic
import com.github.freshmorsikov.tutor.domain.Topic
import com.github.freshmorsikov.tutor.domain.Video
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun TopicLLM.toModel(id: String): Topic {
    return Topic(
        id = id,
        title = topic,
        overview = overview,
        videos = videos.map { video ->
            Video(
                title = video.title,
                url = video.url,
            )
        },
        subtopics = subtopics.map { subtopic ->
            Subtopic(
                id = Uuid.random().toString(),
                title = subtopic,
                isExplored = false,
            )
        },
    )
}