package com.redinnovationlabs.redinnovationnda.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SPLASH_DURATION_MILLIS = 1800L

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoScale = remember { Animatable(0.88f) }
    val logoAlpha = remember { Animatable(0f) }
    val accentProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            accentProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1100, easing = LinearEasing)
            )
        }
        delay(SPLASH_DURATION_MILLIS.milliseconds)
        onFinished()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFAFAFA)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF8F8F8),
                            RedNdaLightGray.copy(alpha = 0.55f)
                        )
                    )
                )
        ) {
            val isLargePortrait = maxWidth >= 600.dp
            val heroWidth = if (isLargePortrait) 620.dp else 356.dp
            val heroPadding = if (isLargePortrait) 22.dp else 16.dp
            val heroRadius = if (isLargePortrait) 36.dp else 28.dp

            SplashBackdrop(accentProgress = accentProgress.value)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isLargePortrait) 36.dp else 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    modifier = Modifier
                        .widthIn(max = heroWidth)
                        .fillMaxWidth()
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                        .graphicsLayer {
                            shadowElevation = if (isLargePortrait) 28.dp.toPx() else 20.dp.toPx()
                        },
                    shape = RoundedCornerShape(heroRadius),
                    color = Color.White.copy(alpha = 0.97f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color(0xFFF7F7F7),
                                        RedNdaLightGray.copy(alpha = 0.52f)
                                    )
                                )
                            )
                            .padding(heroPadding)
                    ) {
                        Canvas(
                            modifier = Modifier.matchParentSize()
                        ) {
                            drawCircle(
                                color = RedNdaRed.copy(alpha = 0.05f),
                                radius = size.minDimension * 0.28f,
                                center = Offset(size.width * 0.88f, size.height * 0.12f)
                            )
                            drawCircle(
                                color = RedNdaBlack.copy(alpha = 0.03f),
                                radius = size.minDimension * 0.24f,
                                center = Offset(size.width * 0.12f, size.height * 0.88f)
                            )
                        }

                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(if (isLargePortrait) 24.dp else 18.dp))
                                    .background(Color.White.copy(alpha = 0.95f))
                                    .padding(if (isLargePortrait) 18.dp else 12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_innovation_labs),
                                    contentDescription = stringResource(R.string.content_description_splash_logo),
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.FillWidth
                                )

                                Canvas(modifier = Modifier.matchParentSize()) {
                                    drawRect(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.White.copy(alpha = 0.20f),
                                                Color.Transparent
                                            )
                                        ),
                                        topLeft = Offset(
                                            size.width * accentProgress.value - size.width * 0.38f,
                                            0f
                                        ),
                                        size = androidx.compose.ui.geometry.Size(
                                            size.width * 0.34f,
                                            size.height
                                        ),
                                        blendMode = BlendMode.Screen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(if (isLargePortrait) 18.dp else 14.dp))

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth(0.36f)
                                    .height(4.dp)
                            ) {
                                drawLine(
                                    color = RedNdaRed,
                                    start = Offset.Zero,
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 4.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SplashBackdrop(
    accentProgress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val majorGrid = 42.dp.toPx()
        val minorGrid = 14.dp.toPx()

        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.24f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += majorGrid
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.24f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += majorGrid
        }

        var mx = 0f
        while (mx <= size.width) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.10f),
                start = Offset(mx, 0f),
                end = Offset(mx, size.height),
                strokeWidth = 0.7f
            )
            mx += minorGrid
        }

        var my = 0f
        while (my <= size.height) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.10f),
                start = Offset(0f, my),
                end = Offset(size.width, my),
                strokeWidth = 0.7f
            )
            my += minorGrid
        }

        val topWidth = size.width * (0.18f + 0.18f * accentProgress)
        drawLine(
            color = RedNdaRed.copy(alpha = 0.95f),
            start = Offset(size.width - topWidth, 0f),
            end = Offset(size.width, size.height * 0.16f),
            strokeWidth = 56.dp.toPx(),
            cap = StrokeCap.Butt
        )
        drawLine(
            color = RedNdaBlack.copy(alpha = 0.9f),
            start = Offset(size.width - topWidth * 0.68f, size.height * 0.08f),
            end = Offset(size.width, size.height * 0.28f),
            strokeWidth = 26.dp.toPx(),
            cap = StrokeCap.Butt
        )
        drawLine(
            color = RedNdaRed.copy(alpha = 0.95f),
            start = Offset(size.width * 0.02f, size.height * 0.90f),
            end = Offset(size.width * (0.18f + 0.08f * accentProgress), size.height),
            strokeWidth = 52.dp.toPx(),
            cap = StrokeCap.Butt
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 1280)
@Composable
private fun SplashScreenPreview() {
    RedNdaTheme {
        SplashScreen(onFinished = {})
    }
}
