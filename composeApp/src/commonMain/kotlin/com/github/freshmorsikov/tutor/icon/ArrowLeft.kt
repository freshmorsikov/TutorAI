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
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

public val Icons.ArrowLeft: ImageVector
    get() {
        if (_arrowLeft != null) {
            return _arrowLeft!!
        }
        _arrowLeft =
            Builder(
                    name = "ArrowLeft",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 21.0.dp,
                    viewportWidth = 25.0f,
                    viewportHeight = 22.0f,
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
                        moveTo(22.625f, 9.09f)
                        curveToRelative(1.035f, 0.0f, 1.875f, 0.856f, 1.875f, 1.91f)
                        reflectiveCurveToRelative(-0.839f, 1.909f, -1.874f, 1.91f)
                        close()
                        moveTo(0.5f, 11.0f)
                        curveToRelative(0.0f, -0.506f, 0.198f, -0.992f, 0.55f, -1.35f)
                        lineToRelative(8.437f, -8.59f)
                        arcToRelative(1.85f, 1.85f, 0.0f, false, true, 2.651f, 0.0f)
                        curveToRelative(0.687f, 0.698f, 0.73f, 1.804f, 0.13f, 2.554f)
                        lineToRelative(-0.13f, 0.145f)
                        lineTo(6.901f, 9.09f)
                        horizontalLineToRelative(15.724f)
                        verticalLineToRelative(3.818f)
                        horizontalLineTo(6.902f)
                        lineToRelative(5.237f, 5.332f)
                        arcToRelative(1.933f, 1.933f, 0.0f, false, true, 0.0f, 2.7f)
                        arcToRelative(1.85f, 1.85f, 0.0f, false, true, -2.651f, 0.0f)
                        lineTo(1.049f, 12.35f)
                        lineToRelative(-0.124f, -0.14f)
                        arcTo(1.93f, 1.93f, 0.0f, false, true, 0.5f, 11.0f)
                    }
                }
                .build()
        return _arrowLeft!!
    }

private var _arrowLeft: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.ArrowLeft, contentDescription = null)
    }
}
