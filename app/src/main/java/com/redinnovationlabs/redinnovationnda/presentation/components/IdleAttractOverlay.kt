package com.redinnovationlabs.redinnovationnda.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import kotlinx.coroutines.launch

@Composable
fun IdleAttractOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(320)) + scaleIn(initialScale = 1.01f),
        exit = fadeOut(animationSpec = tween(220)) + scaleOut(targetScale = 1.01f),
        modifier = modifier.fillMaxSize()
    ) {
        IdleAttractContent(onDismiss = onDismiss)
    }
}

@Composable
private fun IdleAttractContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoScale = remember { Animatable(0.94f) }
    val logoAlpha = remember { Animatable(0f) }
    val accentProgress = remember { Animatable(0f) }
    val pulseTransition = rememberInfiniteTransition(label = "idleAttractPulse")
    val pulse by pulseTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleAttractHintPulse"
    )
    val accentMotion by pulseTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleAttractAccentMotion"
    )

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
                animationSpec = tween(durationMillis = 720, easing = FastOutSlowInEasing)
            )
        }
        launch {
            accentProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200, easing = LinearEasing)
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onDismiss() })
            }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFBFBFB),
                        Color(0xFFF4F4F5)
                    )
                )
            )
    ) {
        val isLargePortrait = maxWidth >= 600.dp
        val heroWidth = if (isLargePortrait) 620.dp else 356.dp
        val heroPadding = if (isLargePortrait) 22.dp else 16.dp
        val heroRadius = if (isLargePortrait) 36.dp else 28.dp

        IdleAttractBackdrop(
            accentProgress = accentProgress.value,
            accentMotion = accentMotion
        )

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
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFF9F9F9),
                                    Color(0xFFF1F1F2)
                                )
                            )
                        )
                        .padding(heroPadding)
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
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
                                .background(Color.White)
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

            Spacer(modifier = Modifier.height(if (isLargePortrait) 34.dp else 26.dp))

            Text(
                text = stringResource(R.string.idle_attract_tap_hint),
                color = RedNdaBlack.copy(alpha = 0.72f),
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                    },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = if (isLargePortrait) 20.sp else 16.sp,
                    letterSpacing = 0.8.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun IdleAttractBackdrop(
    accentProgress: Float,
    accentMotion: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val majorGrid = 42.dp.toPx()
        val minorGrid = 14.dp.toPx()
        val motion = accentMotion.dp.toPx()

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

        val topSlide = (1f - accentProgress) * 180.dp.toPx()
        val topRed = Path().apply {
            moveTo(size.width * 0.66f + topSlide + motion, -40.dp.toPx())
            lineTo(size.width * 0.84f + topSlide + motion, -40.dp.toPx())
            lineTo(size.width + 42.dp.toPx() + topSlide + motion, size.height * 0.14f)
            lineTo(size.width * 0.90f + topSlide + motion, size.height * 0.19f)
            close()
        }
        drawPath(topRed, RedNdaRed.copy(alpha = 0.94f))

        val topBlack = Path().apply {
            moveTo(size.width * 0.78f + topSlide + motion, size.height * 0.08f)
            lineTo(size.width * 0.84f + topSlide + motion, size.height * 0.06f)
            lineTo(size.width + 24.dp.toPx() + topSlide + motion, size.height * 0.28f)
            lineTo(size.width * 0.96f + topSlide + motion, size.height * 0.31f)
            close()
        }
        drawPath(topBlack, RedNdaBlack.copy(alpha = 0.90f))

        val bottomSlide = (1f - accentProgress) * 160.dp.toPx()
        val bottomRed = Path().apply {
            moveTo(size.width * 0.76f + bottomSlide - motion, size.height + 48.dp.toPx())
            lineTo(size.width * 0.94f + bottomSlide - motion, size.height + 48.dp.toPx())
            lineTo(size.width + 36.dp.toPx() + bottomSlide - motion, size.height * 0.84f)
            lineTo(size.width * 0.88f + bottomSlide - motion, size.height * 0.79f)
            close()
        }
        drawPath(bottomRed, RedNdaRed.copy(alpha = 0.90f))

        val bottomBlack = Path().apply {
            moveTo(size.width * 0.86f + bottomSlide - motion, size.height + 28.dp.toPx())
            lineTo(size.width * 0.96f + bottomSlide - motion, size.height + 28.dp.toPx())
            lineTo(size.width + 20.dp.toPx() + bottomSlide - motion, size.height * 0.91f)
            lineTo(size.width * 0.92f + bottomSlide - motion, size.height * 0.87f)
            close()
        }
        drawPath(bottomBlack, RedNdaBlack.copy(alpha = 0.82f))

        drawCircle(
            color = RedNdaRed.copy(alpha = 0.035f),
            radius = size.minDimension * 0.24f,
            center = Offset(size.width * 0.78f, size.height * 0.36f)
        )
    }
}
