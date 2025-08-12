package com.github.freshmorsikov.tutor.agent

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.prompt.structure.json.JsonSchemaGenerator
import ai.koog.prompt.structure.json.JsonStructuredData
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
    @property:LLMDescription("Videos for learning")
    val videos: String,
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
        videos = "https://youtu.be/nVyD6THc123",
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
        videos = "https://youtu.be/nVyD6THcABC",
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
        videos = "https://youtu.be/nVyD6THc1As",
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