package com.github.freshmorsikov.tutor.presentation

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.dsl.builder.AIAgentNodeDelegate
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequestStructured
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import ai.koog.prompt.structure.StructuredResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.tutor.LearningPlan
import com.github.freshmorsikov.tutor.learningPlanStructure
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tutor.composeApp.BuildConfig
import kotlin.uuid.ExperimentalUuidApi

private const val ROOT_ID = "root"

class MainViewModel : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.WaitingForTopic())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val subtopicChannel: Channel<String> = Channel()

    private val mainStrategy = strategy<String, String>(name = "Main") {
        val nodeCallLLM by nodeLLMRequestStructured(
            structure = learningPlanStructure,
            retries = 3,
            fixingModel = OpenAIModels.Chat.GPT4o,
        )
        val nodeUpdateState by nodeUpdateState()
        val nodeSubtopicDiving by nodeSubtopicDiving()

        edge(nodeStart forwardTo nodeCallLLM)
        edge(nodeCallLLM forwardTo nodeUpdateState)
        edge(nodeUpdateState forwardTo nodeSubtopicDiving)
        edge(nodeSubtopicDiving forwardTo nodeCallLLM onCondition { it.isSuccess } transformed { it.getOrNull() ?: "" })
        edge(nodeUpdateState forwardTo nodeFinish onCondition { false })
    }
    private val agent = AIAgent(
        executor = simpleOpenAIExecutor(BuildConfig.OPENAI_API_KEY),
        llmModel = OpenAIModels.Chat.GPT4o,
        strategy = mainStrategy,
        systemPrompt = """
                You are an expert tutor that helps generate high-level learning plan by given topic.
                When user gives you topic for learning:
                1. Give a brief overview of the topic.
                2. Make a high-level plan for studying this topic with list of subtopics subtopics to dive deeper.
                
                Always respond with a JSON object containing a fields: 'topic', 'overview' and 'subtopics'
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
        viewModelScope.launch {
            subtopicChannel.send(subtopicId)
            _state.update {
                val currentData = it as? MainState.Data ?: return@update it

                currentData.copy(loadingSubtopicId = subtopicId)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun AIAgentSubgraphBuilderBase<*, *>.nodeUpdateState(): AIAgentNodeDelegate<Result<StructuredResponse<LearningPlan>>, String> =
        node("updateState") { input ->
            input.onSuccess { data ->
                _state.update {
                    val previousData = it as? MainState.Data
                    val topicChain = if (previousData == null) {
                        data.structure.topic
                    } else {
                        "${previousData.topicChain} > ${data.structure.topic}"
                    }
                    MainState.Data(
                        topicChain = topicChain,
                        currentLearningNode = createLearningNode(
                            id = previousData?.loadingSubtopicId ?: ROOT_ID,
                            topic = data.structure.topic,
                            subtopics = data.structure.subtopics,
                            overview = data.structure.overview,
                            parent = previousData?.currentLearningNode
                        ),
                        loadingSubtopicId = null
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
                    currentValue.currentLearningNode.subtopics.find { subtopic ->
                        subtopic.id == subtopicId
                    }?.let { subtopic ->
                        Result.success(value = subtopic.topic)
                    } ?: Result.failure(exception = Exception("No such topic found"))
                }

                else -> {
                    Result.failure(exception = Exception("Data not found"))
                }

            }
        }

}