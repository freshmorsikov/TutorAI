package com.github.freshmorsikov.tutor.data.video

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import tutor.composeApp.BuildConfig

class ApiService() {

    private val httpClient = HttpClient {
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url {
                protocol = URLProtocol.HTTPS
                host = "www.googleapis.com"
                parameters.append("key", BuildConfig.GOOGLE_API_KEY)
            }
        }

        install(Logging) {
            logger = NetworkLogger
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
    }

    suspend fun getVideoList(topic: String): Result<VideoResponse> {
        return safeApiCall {
            httpClient.get {
                url {
                    path("youtube/v3/search")
                    parameter("part", "snippet")
                    parameter("type", "video")
                    parameter("maxResults", 10)
                    parameter("q", topic)
                }
            }
        }
    }

    private suspend inline fun <reified T> safeApiCall(
        block: () -> HttpResponse,
    ): Result<T> {
        return try {
            val response = block()
            if (response.status.isSuccess()) {
                Result.success(
                    value = response.body<T>()
                )
            } else {
                val errorMessage = response.body<String>()
                Result.failure(
                    exception = Exception(errorMessage)
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure(exception)
        }
    }

}