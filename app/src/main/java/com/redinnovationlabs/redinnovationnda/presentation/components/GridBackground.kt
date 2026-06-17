package com.redinnovationlabs.redinnovationnda.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray

@Composable
fun GridBackground(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val gridSpacing = 40.dp.toPx()
        val gridColor = RedNdaBorderGray.copy(alpha = 0.22f)
        val scratchColor = Color(0xFF8A8A8A).copy(alpha = 0.18f)

        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += gridSpacing
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += gridSpacing
        }

        val scratches = listOf(
            Offset(size.width * 0.10f, size.height * 0.12f) to Offset(size.width * 0.48f, size.height * 0.18f),
            Offset(size.width * 0.18f, size.height * 0.58f) to Offset(size.width * 0.72f, size.height * 0.67f),
            Offset(size.width * 0.52f, size.height * 0.06f) to Offset(size.width * 0.84f, size.height * 0.18f),
            Offset(size.width * 0.66f, size.height * 0.36f) to Offset(size.width * 0.98f, size.height * 0.42f),
            Offset(size.width * 0.06f, size.height * 0.82f) to Offset(size.width * 0.38f, size.height * 0.92f)
        )

        scratches.forEachIndexed { index, (start, end) ->
            drawLine(
                color = scratchColor.copy(alpha = if (index % 2 == 0) 0.18f else 0.12f),
                start = start,
                end = end,
                strokeWidth = if (index % 2 == 0) 2f else 1.2f
            )
        }

        drawCircle(
            color = RedNdaLightGray.copy(alpha = 0.06f),
            radius = size.minDimension * 0.32f,
            center = Offset(size.width * 0.80f, size.height * 0.18f)
        )
    }
}
