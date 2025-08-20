package com.github.freshmorsikov.tutor.data.video

import io.ktor.client.plugins.logging.Logger

object NetworkLogger : Logger {
    override fun log(message: String) {
        println("Ktor: $message")
    }
}