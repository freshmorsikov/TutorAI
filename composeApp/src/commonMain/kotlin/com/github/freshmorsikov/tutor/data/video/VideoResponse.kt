package com.github.freshmorsikov.tutor.data.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    @SerialName("items") val items: List<VideoItem>,
)

@Serializable
data class VideoItem(
    @SerialName("id") val id: VideoId,
    @SerialName("snippet") val snippet: VideoSnippet,
)

@Serializable
data class VideoId(
    @SerialName("videoId") val videoId: String?,
)

@Serializable
data class VideoSnippet(
    @SerialName("title") val title: String,
)