package com.github.freshmorsikov.tutor.agent.node

import ai.koog.agents.core.dsl.builder.AIAgentBuilderDslMarker
import ai.koog.agents.core.dsl.builder.AIAgentNodeDelegate
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.environment.ReceivedToolResult
import ai.koog.agents.core.environment.result
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.structure.StructuredData
import ai.koog.prompt.structure.StructuredResponse

@AIAgentBuilderDslMarker
inline fun <reified T> AIAgentSubgraphBuilderBase<*, *>.nodeLLMStructuredSendToolResult(
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