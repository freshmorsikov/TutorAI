package com.github.freshmorsikov.tutor

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.w3c.dom.HTMLTitleElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val htmlTitleElement = (
            document.head!!.getElementsByTagName("title").item(0)
                ?: document.createElement("title").also { document.head!!.appendChild(it) }
            ) as HTMLTitleElement
    htmlTitleElement.textContent = "Tutor AI"
    ComposeViewport(document.body!!) {
        App()
    }
}