package com.github.freshmorsikov.tutor.agent

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.prompt.structure.json.JsonSchemaGenerator
import ai.koog.prompt.structure.json.JsonStructuredData
import com.github.freshmorsikov.tutor.agent.tool.VideoTool
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LearningPlan")
@LLMDescription("Learning plan on the topic")
data class TopicLLM(
    @property:LLMDescription("Main topic for learning")
    val topic: String,
    @property:LLMDescription("A brief overview of the topic")
    val overview: String,
    @property:LLMDescription("Video list for learning")
    val videos: List<VideoTool.Result.Video>,
    @property:LLMDescription("List of subtopics of this learning plan (5 maximum)")
    val subtopics: List<String>,
)

val topicExamples = listOf(
    TopicLLM(
        topic = "Computer science",
        overview = """
            Computer science is the study of computers and computational systems, encompassing their theory, design, development, and application. 
            It involves the study of algorithms, programming languages, software and hardware development, and various theoretical and practical aspects of computation.
        """.trimIndent(),
        videos = listOf(
            VideoTool.Result.Video(
                title = "Crash Course Computer Science",
                url = "https://www.youtube.com/watch?v=tpIctyqH29Q",
            ),
            VideoTool.Result.Video(
                title = "Map of Computer Science",
                url = "https://www.youtube.com/watch?v=SzJ46YA_RaA",
            ),
        ),
        subtopics = listOf(
            "Algorithms and Data Structures",
            "Programming Languages",
            "Software Development",
            "Computer Architecture",
            "Cybersecurity",
        ),
    ),
    TopicLLM(
        topic = "Design",
        overview = """
            Design is a multifaceted field focused on problem-solving and creating solutions, often involving both aesthetic and functional considerations. 
            It encompasses the planning and execution of ideas into tangible or intangible products, systems, or experiences. 
            Ultimately, design aims to improve human experiences, whether through the creation of physical objects, digital interfaces, or even abstract concepts. 
        """.trimIndent(),
        videos = listOf(
            VideoTool.Result.Video(
                title = "The Principles of Design | FREE COURSE",
                url = "https://www.youtube.com/watch?v=9EPTM91TBDU",
            ),
            VideoTool.Result.Video(
                title = "Graphic Design Basics | FREE COURSE",
                url = "https://www.youtube.com/watch?v=GQS7wPujL2k",
            ),
            VideoTool.Result.Video(
                title = "Learn Graphic Design by Yourself: How to Become a Graphic Designer",
                url = "https://www.youtube.com/watch?v=9Rz2DWRcmH8",
            ),
        ),
        subtopics = listOf(
            "Design Principles",
            "Theory and Concepts",
            "Design software",
            "Building a Portfolio",
        )
    ),
    TopicLLM(
        topic = "Marketing",
        overview = "Marketing is the process of creating, communicating, and delivering value to customers to build strong relationships that benefit the organization.",
        videos = listOf(
            VideoTool.Result.Video(
                title = "What Is Marketing In 3 Minutes | Marketing For Beginners",
                url = "https://www.youtube.com/watch?v=QusJ4fpWQwA",
            ),
            VideoTool.Result.Video(
                title = "Digital Marketing 101 (A Beginner’s Guide To Marketing)",
                url = "https://www.youtube.com/watch?v=h95cQkEWBx0",
            ),
        ),
        subtopics = listOf(
            "Market Research",
            "Target Audience Identification",
            "Customer Segmentation",
            "Developing Products and Services",
            "Brand Building",
        )
    ),
)

val topicStructure = JsonStructuredData.createJsonStructure<TopicLLM>(
    schemaFormat = JsonSchemaGenerator.SchemaFormat.JsonSchema,
    examples = topicExamples,
    schemaType = JsonStructuredData.JsonSchemaType.SIMPLE
)