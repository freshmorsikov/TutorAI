package com.github.freshmorsikov.tutor.presentation.mapper

import com.github.freshmorsikov.tutor.data.Node
import com.github.freshmorsikov.tutor.presentation.MainState

fun Node.toSubtopic(): MainState.Subtopic {
    return MainState.Subtopic(
        id = id,
        title = title,
        isLoading = false,
        isExplored = this is Node.Explored,
    )
}