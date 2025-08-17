package com.github.freshmorsikov.tutor

import kotlinx.browser.window

actual fun openLink(link: String) {
    window.open(url = link)
}