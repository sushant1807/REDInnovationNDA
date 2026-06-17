package com.redinnovationlabs.redinnovationnda.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed

enum class DiagonalAccentPosition {
    TopEnd,
    BottomStart
}

@Composable
fun DiagonalAccent(
    modifier: Modifier = Modifier,
    position: DiagonalAccentPosition,
    width: Dp = 78.dp,
    height: Dp = 120.dp,
    color: Color = RedNdaRed
) {
    Canvas(modifier = modifier.size(width = width, height = height)) {
        val path = Path().apply {
            when (position) {
                DiagonalAccentPosition.TopEnd -> {
                    moveTo(size.width * 0.55f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width * 0.45f, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                DiagonalAccentPosition.BottomStart -> {
                    moveTo(0f, 0f)
                    lineTo(size.width * 0.55f, 0f)
                    lineTo(size.width, size.height)
                    lineTo(size.width * 0.45f, size.height)
                    close()
                }
            }
        }
        drawPath(path = path, color = color)
    }
}
