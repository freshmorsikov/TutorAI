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

public val Icons.ArrowUp: ImageVector
    get() {
        if (_arrowUp != null) {
            return _arrowUp!!
        }
        _arrowUp =
            Builder(
                    name = "ArrowUp",
                    defaultWidth = 21.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 21.0f,
                    viewportHeight = 24.0f,
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
                        moveTo(12.41f, 22.125f)
                        curveToRelative(0.0f, 1.035f, -0.856f, 1.875f, -1.91f, 1.875f)
                        reflectiveCurveToRelative(-1.909f, -0.839f, -1.91f, -1.874f)
                        close()
                        moveTo(10.5f, 0.0f)
                        curveToRelative(0.506f, 0.0f, 0.992f, 0.198f, 1.35f, 0.55f)
                        lineToRelative(8.59f, 8.437f)
                        curveToRelative(0.746f, 0.732f, 0.746f, 1.92f, 0.0f, 2.651f)
                        arcToRelative(1.935f, 1.935f, 0.0f, false, true, -2.554f, 0.13f)
                        lineToRelative(-0.145f, -0.13f)
                        lineToRelative(-5.332f, -5.237f)
                        verticalLineToRelative(15.724f)
                        horizontalLineTo(8.591f)
                        verticalLineTo(6.402f)
                        lineToRelative(-5.332f, 5.237f)
                        arcToRelative(1.934f, 1.934f, 0.0f, false, true, -2.7f, 0.0f)
                        arcToRelative(1.85f, 1.85f, 0.0f, false, true, 0.0f, -2.651f)
                        lineTo(9.15f, 0.549f)
                        lineToRelative(0.14f, -0.124f)
                        arcTo(1.93f, 1.93f, 0.0f, false, true, 10.5f, 0.0f)
                    }
                }
                .build()
        return _arrowUp!!
    }

private var _arrowUp: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.ArrowUp, contentDescription = null)
    }
}
