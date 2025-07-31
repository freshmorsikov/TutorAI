package com.github.freshmorsikov.tutor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform