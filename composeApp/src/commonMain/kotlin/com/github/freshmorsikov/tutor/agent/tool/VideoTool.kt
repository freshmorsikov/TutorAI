package com.github.freshmorsikov.tutor.agent.tool

import ai.koog.agents.core.tools.*
import com.github.freshmorsikov.tutor.data.video.ApiService
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

object VideoTool : Tool<VideoTool.Args, VideoTool.Result>() {

    @Serializable
    data class Args(val topic: String) : ToolArgs

    @Serializable
    data class Result(
        val videos: List<Video>,
    ) : ToolResult.JSONSerializable<Result> {

        @Serializable
        data class Video(
            val title: String,
            val url: String,
        )

        override fun getSerializer(): KSerializer<Result> {
            return serializer()
        }
    }

    private val apiService = ApiService()

    override val argsSerializer: KSerializer<Args> = Args.serializer()

    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "get_videos", description = "Service tool, used by the agent to get videos for some topic.",
        requiredParameters = listOf(
            ToolParameterDescriptor(
                name = "topic",
                description = "Topic for learning",
                type = ToolParameterType.String,
            ),
        ),
    )

    override suspend fun execute(args: Args): Result {
        val result = apiService.getVideoList(topic = args.topic).getOrNull()
        return Result(
            videos = result?.items?.mapNotNull { videoItem ->
                videoItem.id.videoId?.let { videoId ->
                    Result.Video(
                        title = videoItem.snippet.title,
                        url = "https://www.youtube.com/watch?v=$videoId",
                    )
                }
            }.orEmpty()
        )
    }

}