package com.github.freshmorsikov.tutor.presentation

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.dsl.builder.*
import ai.koog.agents.core.dsl.extension.nodeExecuteTool
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.onToolCall
import ai.koog.agents.core.environment.ReceivedToolResult
import ai.koog.agents.core.environment.result
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.structure.StructuredData
import ai.koog.prompt.structure.StructuredResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.tutor.agent.TopicLLM
import com.github.freshmorsikov.tutor.agent.tool.VideoTool
import com.github.freshmorsikov.tutor.agent.topicStructure
import com.github.freshmorsikov.tutor.data.LearningRepository
import com.github.freshmorsikov.tutor.data.Node
import com.github.freshmorsikov.tutor.presentation.MainState.Subtopic
import com.github.freshmorsikov.tutor.presentation.mapper.toModel
import com.github.freshmorsikov.tutor.presentation.mapper.toSubtopic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tutor.composeApp.BuildConfig
import kotlin.uuid.ExperimentalUuidApi

private const val ROOT_ID = "root"

class MainViewModel(
    private val learningRepository: LearningRepository = LearningRepository(),
) : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.WaitingForTopic())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val subtopicChannel: Channel<String> = Channel()

    private val mainStrategy = strategy<String, String>(name = "Main") {
        val nodeCallLLM by nodeLLMRequest()
        val callTool by nodeExecuteTool()
        val sendToolResult by nodeLLMStructuredSendToolResult(
            structure = topicStructure,
            retries = 3,
            fixingModel = OpenAIModels.Chat.GPT4o,
        )

        val nodeUpdateState by nodeUpdateState()
        val nodeSubtopicDiving by nodeSubtopicDiving()

        edge(nodeStart forwardTo nodeCallLLM)
        edge(nodeCallLLM forwardTo callTool onToolCall { true })
        edge(callTool forwardTo sendToolResult)
        edge(sendToolResult forwardTo nodeUpdateState)
        edge(nodeUpdateState forwardTo nodeSubtopicDiving)
        edge(nodeSubtopicDiving forwardTo nodeCallLLM onCondition { it.isSuccess } transformed { it.getOrNull() ?: "" })
        edge(nodeUpdateState forwardTo nodeFinish onCondition { false })
    }
    val toolRegistry = ToolRegistry {
        tool(VideoTool)
    }
    private val agent = AIAgent(
        executor = simpleOpenAIExecutor(BuildConfig.OPENAI_API_KEY),
        llmModel = OpenAIModels.Chat.GPT4o,
        strategy = mainStrategy,
        toolRegistry = toolRegistry,
        systemPrompt = """
                You are an expert tutor that helps generate high-level learning plan by given topic.
                When user gives you topic for learning:
                1. Give a brief overview of the topic.
                2. Find learning videos by the topic.
                3. Make a high-level plan for studying this topic with list of subtopics subtopics to dive deeper.
                
                Always respond with a JSON object containing a fields: 'topic', 'overview', 'videos' and 'subtopics'
            """
    )

    fun updateInput(input: String) {
        _state.update {
            val currentState = it as? MainState.WaitingForTopic ?: return@update it
            currentState.copy(input = input)
        }
    }

    fun start() {
        val currentState = _state.value as? MainState.WaitingForTopic ?: return
        if (currentState.input.isBlank()) {
            return
        }

        _state.update {
            currentState.copy(isLoading = true)
        }

        viewModelScope.launch {
            agent.run(agentInput = currentState.input)
        }
    }

    fun selectSubtopic(subtopicId: String) {
        val currentData = _state.value as? MainState.Data ?: return

        val switchedNode = learningRepository.switchToExplored(id = subtopicId)
        if (switchedNode == null) {
            viewModelScope.launch {
                subtopicChannel.send(subtopicId)
                _state.update {
                    currentData.copy(
                        subtopics = currentData.subtopics.map { subtopic ->
                            subtopic.copy(isLoading = subtopic.id == subtopicId)
                        }
                    )
                }
            }
        } else {
            val topicChainItem = MainState.TopicChainItem(
                id = switchedNode.id,
                title = switchedNode.title
            )
            _state.update {
                currentData.copy(
                    topicChain = currentData.topicChain + topicChainItem,
                    topic = switchedNode.topic,
                    subtopics = switchedNode.subtopics.map(Node::toSubtopic),
                )
            }
        }
    }

    fun goToPreviousTopic(topicId: String) {
        val currentData = _state.value as? MainState.Data ?: return
        if (topicId == currentData.topicChain.last().id) {
            return
        }

        _state.update {
            val currentData = it as? MainState.Data ?: return@update it
            val parentNode = learningRepository.switchToParent(topicId = topicId) ?: return@update it

            currentData.copy(
                topicChain = currentData.topicChain.dropLastWhile { topicChainItem ->
                    topicChainItem.id != parentNode.id
                },
                topic = parentNode.topic,
                subtopics = parentNode.subtopics.map(Node::toSubtopic),
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun AIAgentSubgraphBuilderBase<*, *>.nodeUpdateState(): AIAgentNodeDelegate<Result<StructuredResponse<TopicLLM>>, String> =
        node("updateState") { input ->
            input.onSuccess { data ->
                _state.update {
                    val currentData = it as? MainState.Data

                    val loadingSubtopic = currentData?.subtopics?.find { subtopic ->
                        subtopic.isLoading
                    }
                    val id = loadingSubtopic?.id ?: ROOT_ID
                    val topicChainItem = MainState.TopicChainItem(
                        id = id,
                        title = data.structure.topic,
                    )
                    val topicChain = currentData?.topicChain ?: emptyList()

                    val newNode = learningRepository.exploreNode(
                        topic = data.structure.toModel(id = id)
                    )
                    MainState.Data(
                        topicChain = topicChain + topicChainItem,
                        topic = newNode.topic,
                        subtopics = newNode.subtopics.map { subtopic ->
                            Subtopic(
                                id = subtopic.id,
                                title = subtopic.title,
                                isLoading = false,
                                isExplored = subtopic is Node.Explored,
                            )
                        }
                    )
                }
            }.onFailure {
                // TODO handle error
            }

            "DONE"
        }

    private fun AIAgentSubgraphBuilderBase<*, *>.nodeSubtopicDiving(): AIAgentNodeDelegate<String, Result<String>> =
        node("subtopicDiving") {
            val subtopicId = subtopicChannel.receive()

            when (val currentValue = _state.value) {
                is MainState.Data -> {
                    currentValue.subtopics.find { subtopic ->
                        subtopic.id == subtopicId
                    }?.let { subtopic ->
                        Result.success(value = subtopic.title)
                    } ?: Result.failure(exception = Exception("No such topic found"))
                }

                else -> {
                    Result.failure(exception = Exception("Data not found"))
                }
            }
        }

    @AIAgentBuilderDslMarker
    private inline fun <reified T> AIAgentSubgraphBuilderBase<*, *>.nodeLLMStructuredSendToolResult(
        name: String? = null,
        structure: StructuredData<T>,
        retries: Int,
        fixingModel: LLModel
    ): AIAgentNodeDelegate<ReceivedToolResult, Result<StructuredResponse<T>>> =
        node(name) { result ->
            llm.writeSession {
                updatePrompt {
                    tool {
                        result(result)
                    }
                }

                requestLLMStructured(
                    structure,
                    retries,
                    fixingModel
                )
            }
        }


}