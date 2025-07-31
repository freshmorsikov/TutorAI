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
        edge(nodeSubtopicDiving forwardTo nodeCallLLM)
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

    fun selectSubtopic(subtopic: String) {
        viewModelScope.launch {
            subtopicChannel.send(subtopic)
        }
    }

    private fun AIAgentSubgraphBuilderBase<*, *>.nodeUpdateState(): AIAgentNodeDelegate<Result<StructuredResponse<LearningPlan>>, String> =
        node("updateState") { input ->
            input.onSuccess { data ->
                _state.update {
                    MainState.Data(
                        topic = data.structure.topic,
                        overview = data.structure.overview,
                        subtopics = data.structure.subtopics,
                    )
                }
            }.onFailure {
                // TODO handle error
            }

            "DONE"
        }

    private fun AIAgentSubgraphBuilderBase<*, *>.nodeSubtopicDiving(): AIAgentNodeDelegate<String, String> =
        node("subtopicDiving") {
            subtopicChannel.receive()
        }

}