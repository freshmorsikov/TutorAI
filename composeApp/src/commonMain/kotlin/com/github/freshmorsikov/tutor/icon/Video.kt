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

public val Icons.Video: ImageVector
    get() {
        if (_video != null) {
            return _video!!
        }
        _video =
            Builder(
                    name = "Video",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 17.0.dp,
                    viewportWidth = 24.0f,
                    viewportHeight = 17.0f,
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
                        moveTo(22.154f, 3.692f)
                        curveTo(22.154f, 3.3f, 21.949f, 2.831f, 21.559f, 2.441f)
                        curveTo(21.169f, 2.051f, 20.7f, 1.846f, 20.308f, 1.846f)
                        horizontalLineTo(3.692f)
                        curveTo(3.3f, 1.846f, 2.831f, 2.051f, 2.441f, 2.441f)
                        curveTo(2.051f, 2.831f, 1.846f, 3.3f, 1.846f, 3.692f)
                        verticalLineTo(12.923f)
                        curveTo(1.846f, 13.315f, 2.051f, 13.785f, 2.441f, 14.174f)
                        curveTo(2.831f, 14.564f, 3.3f, 14.769f, 3.692f, 14.769f)
                        horizontalLineTo(20.308f)
                        curveTo(20.7f, 14.769f, 21.169f, 14.564f, 21.559f, 14.174f)
                        curveTo(21.949f, 13.785f, 22.154f, 13.315f, 22.154f, 12.923f)
                        verticalLineTo(3.692f)
                        close()
                        moveTo(24.0f, 12.923f)
                        curveTo(24.0f, 13.916f, 23.513f, 14.831f, 22.864f, 15.48f)
                        curveTo(22.215f, 16.128f, 21.3f, 16.615f, 20.308f, 16.615f)
                        horizontalLineTo(3.692f)
                        curveTo(2.7f, 16.615f, 1.785f, 16.128f, 1.136f, 15.48f)
                        curveTo(0.487f, 14.831f, 0.0f, 13.916f, 0.0f, 12.923f)
                        verticalLineTo(3.692f)
                        curveTo(0.0f, 2.7f, 0.487f, 1.785f, 1.136f, 1.136f)
                        curveTo(1.785f, 0.487f, 2.7f, 0.0f, 3.692f, 0.0f)
                        horizontalLineTo(20.308f)
                        curveTo(21.3f, 0.0f, 22.215f, 0.487f, 22.864f, 1.136f)
                        curveTo(23.513f, 1.785f, 24.0f, 2.7f, 24.0f, 3.692f)
                        verticalLineTo(12.923f)
                        close()
                    }
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(10.203f, 4.713f)
                        curveTo(10.515f, 4.556f, 10.889f, 4.591f, 11.169f, 4.8f)
                        lineTo(14.861f, 7.569f)
                        curveTo(15.094f, 7.744f, 15.231f, 8.017f, 15.231f, 8.308f)
                        curveTo(15.231f, 8.598f, 15.094f, 8.872f, 14.861f, 9.046f)
                        lineTo(11.169f, 11.815f)
                        curveTo(10.889f, 12.025f, 10.515f, 12.059f, 10.203f, 11.903f)
                        curveTo(9.89f, 11.746f, 9.692f, 11.427f, 9.692f, 11.077f)
                        verticalLineTo(5.539f)
                        lineTo(9.701f, 5.409f)
                        curveTo(9.744f, 5.111f, 9.929f, 4.85f, 10.203f, 4.713f)
                        close()
                    }
                }
                .build()
        return _video!!
    }

private var _video: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.Video, contentDescription = null)
    }
}
