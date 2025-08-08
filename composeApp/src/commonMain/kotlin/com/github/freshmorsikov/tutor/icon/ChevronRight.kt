package com.github.freshmorsikov.tutor.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

public val Icons.ChevronRight: ImageVector
    get() {
        if (_chevronRight != null) {
            return _chevronRight!!
        }
        _chevronRight =
            Builder(
                    name = "ChevronRight",
                    defaultWidth = 9.0.dp,
                    defaultHeight = 16.0.dp,
                    viewportWidth = 9.0f,
                    viewportHeight = 16.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(8.594f, 9.028f)
                        curveTo(8.854f, 8.756f, 9.0f, 8.386f, 9.0f, 8.0f)
                        curveTo(9.0f, 7.606f, 8.844f, 7.26f, 8.594f, 6.972f)
                        lineTo(2.364f, 0.426f)
                        curveTo(1.823f, -0.142f, 0.946f, -0.142f, 0.406f, 0.426f)
                        curveTo(-0.135f, 0.994f, -0.135f, 1.915f, 0.406f, 2.483f)
                        curveTo(1.754f, 3.899f, 4.562f, 6.972f, 4.562f, 6.972f)
                        lineTo(5.571f, 8.013f)
                        lineTo(4.586f, 9.028f)
                        curveTo(4.586f, 9.028f, 1.768f, 12.086f, 0.406f, 13.517f)
                        lineTo(0.31f, 13.627f)
                        curveTo(-0.133f, 14.199f, -0.101f, 15.041f, 0.406f, 15.574f)
                        curveTo(0.946f, 16.142f, 1.823f, 16.142f, 2.364f, 15.574f)
                        lineTo(8.594f, 9.028f)
                        close()
                    }
                }
                .build()
        return _chevronRight!!
    }

private var _chevronRight: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.ChevronRight, contentDescription = null)
    }
}
