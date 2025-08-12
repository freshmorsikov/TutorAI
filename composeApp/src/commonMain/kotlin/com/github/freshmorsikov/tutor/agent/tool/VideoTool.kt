package com.github.freshmorsikov.tutor.agent.tool

import ai.koog.agents.core.tools.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

object VideoTool : SimpleTool<VideoTool.Args>() {

    @Serializable
    data class Args(val topic: String) : ToolArgs

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

    override suspend fun doExecute(args: Args): String {
        return "https://youtu.be/V_1fM9eRt_4"
    }
}