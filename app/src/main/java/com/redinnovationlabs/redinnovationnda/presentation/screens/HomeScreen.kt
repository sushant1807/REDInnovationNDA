package com.redinnovationlabs.redinnovationnda.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.presentation.components.RedPrimaryButton
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaCircuitLine
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaDarkGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRedDark
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaWhite
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onProceedClick: () -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NdaHomeScreen(
        uiState = uiState,
        onProceedClick = onProceedClick,
        onScanClick = onScanClick,
        modifier = modifier
    )
}

@Composable
fun NdaHomeScreen(
    uiState: HomeUiState,
    onProceedClick: () -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoProgress = remember { Animatable(0f) }
    val headerProgress = remember { Animatable(0f) }
    val firstCardProgress = remember { Animatable(0f) }
    val secondCardProgress = remember { Animatable(0f) }
    val footerProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            logoProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 480, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(80.milliseconds)
            headerProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(170.milliseconds)
            firstCardProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 560, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(280.milliseconds)
            secondCardProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 560, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(400.milliseconds)
            footerProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 440, easing = FastOutSlowInEasing)
            )
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = RedNdaWhite
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val isTabletPortrait = maxWidth >= 600.dp
            val horizontalPadding = if (isTabletPortrait) 32.dp else 22.dp
            val topPadding = if (isTabletPortrait) 24.dp else 18.dp
            val bottomPadding = if (isTabletPortrait) 18.dp else 14.dp
            val cardMaxWidth = if (isTabletPortrait) 560.dp else 460.dp
            val logoOffsetPx = with(LocalDensity.current) { 18.dp.toPx() }
            val headerOffsetPx = with(LocalDensity.current) { 22.dp.toPx() }
            val cardOffsetPx = with(LocalDensity.current) { 28.dp.toPx() }
            val footerOffsetPx = with(LocalDensity.current) { 18.dp.toPx() }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                RedNdaWhite,
                                Color(0xFFFDFDFD),
                                Color(0xFFFAFAFA)
                            )
                        )
                    )
            ) {
                TechBackground()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = horizontalPadding,
                            end = horizontalPadding,
                            top = topPadding,
                            bottom = bottomPadding
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BrandLogo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                alpha = logoProgress.value
                                translationY = logoOffsetPx * (1f - logoProgress.value)
                                scaleX = 0.97f + (0.03f * logoProgress.value)
                                scaleY = 0.97f + (0.03f * logoProgress.value)
                            }
                    )

                    Spacer(modifier = Modifier.height(if (isTabletPortrait) 16.dp else 12.dp))

                    WelcomeHeader(
                        title = uiState.welcomeTitle,
                        subtitle = uiState.welcomeSubtitle,
                        modifier = Modifier
                            .widthIn(max = 560.dp)
                            .graphicsLayer {
                                alpha = headerProgress.value
                                translationY = headerOffsetPx * (1f - headerProgress.value)
                                scaleX = 0.975f + (0.025f * headerProgress.value)
                                scaleY = 0.975f + (0.025f * headerProgress.value)
                            }
                    )

                    Spacer(modifier = Modifier.height(if (isTabletPortrait) 24.dp else 12.dp))

                    NdaOptionCard(
                        modifier = Modifier
                            .widthIn(max = cardMaxWidth)
                            .graphicsLayer {
                                alpha = firstCardProgress.value
                                translationY = cardOffsetPx * (1f - firstCardProgress.value)
                                scaleX = 0.965f + (0.035f * firstCardProgress.value)
                                scaleY = 0.965f + (0.035f * firstCardProgress.value)
                            },
                        iconRes = R.drawable.ic_document_pen,
                        title = buildAnnotatedString {
                            append(uiState.proceedLineOne)
                            append("\n")
                            withStyle(SpanStyle(color = RedNdaRed, fontWeight = FontWeight.ExtraBold)) {
                                append(uiState.proceedAccent)
                            }
                            append(" ")
                            append(uiState.proceedLineTwo)
                        },
                        description = uiState.proceedDescription,
                        buttonText = uiState.proceedButtonText,
                        buttonIconRes = R.drawable.ic_arrow_right,
                        onClick = onProceedClick
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    NdaOptionCard(
                        modifier = Modifier
                            .widthIn(max = cardMaxWidth)
                            .graphicsLayer {
                                alpha = secondCardProgress.value
                                translationY = cardOffsetPx * (1f - secondCardProgress.value)
                                scaleX = 0.965f + (0.035f * secondCardProgress.value)
                                scaleY = 0.965f + (0.035f * secondCardProgress.value)
                            },
                        iconRes = R.drawable.ic_qr_scan,
                        title = buildAnnotatedString {
                            withStyle(SpanStyle(color = RedNdaRed, fontWeight = FontWeight.ExtraBold)) {
                                append(uiState.scanAccent)
                            }
                            append(" ")
                            append(uiState.scanLineTwo)
                        },
                        description = uiState.scanDescription,
                        buttonText = uiState.scanButtonText,
                        buttonIconRes = R.drawable.ic_qr_small,
                        onClick = onScanClick
                    )

                    Spacer(modifier = Modifier.height(if (isTabletPortrait) 36.dp else 12.dp))

                    FooterBrand(
                        modifier = Modifier.graphicsLayer {
                            alpha = footerProgress.value
                            translationY = footerOffsetPx * (1f - footerProgress.value)
                        }
                    )
                    Spacer(modifier = Modifier.height(if (isTabletPortrait) 12.dp else 10.dp))
                }
            }
        }
    }
}

@Composable
fun TechBackground(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val majorGrid = 40.dp.toPx()
        val minorGrid = 16.dp.toPx()

        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.48f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += majorGrid
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.48f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += majorGrid
        }

        var mx = 0f
        while (mx <= size.width) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.18f),
                start = Offset(mx, 0f),
                end = Offset(mx, size.height),
                strokeWidth = 0.7f
            )
            mx += minorGrid
        }

        var my = 0f
        while (my <= size.height) {
            drawLine(
                color = RedNdaBorderGray.copy(alpha = 0.18f),
                start = Offset(0f, my),
                end = Offset(size.width, my),
                strokeWidth = 0.7f
            )
            my += minorGrid
        }

        val circuitPath = Path().apply {
            moveTo(size.width * 0.66f, size.height * 0.06f)
            lineTo(size.width * 0.86f, size.height * 0.06f)
            lineTo(size.width * 0.86f, size.height * 0.13f)
            lineTo(size.width * 0.78f, size.height * 0.13f)
            lineTo(size.width * 0.78f, size.height * 0.22f)
            lineTo(size.width * 0.92f, size.height * 0.22f)
            lineTo(size.width * 0.92f, size.height * 0.28f)
        }
        drawPath(
            path = circuitPath,
            color = RedNdaCircuitLine.copy(alpha = 0.75f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )

        val lowerCircuit = Path().apply {
            moveTo(size.width * 0.06f, size.height * 0.86f)
            lineTo(size.width * 0.18f, size.height * 0.86f)
            lineTo(size.width * 0.18f, size.height * 0.80f)
            lineTo(size.width * 0.26f, size.height * 0.80f)
            lineTo(size.width * 0.26f, size.height * 0.74f)
        }
        drawPath(
            path = lowerCircuit,
            color = RedNdaCircuitLine.copy(alpha = 0.62f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )

        listOf(
            Offset(size.width * 0.86f, size.height * 0.06f),
            Offset(size.width * 0.78f, size.height * 0.13f),
            Offset(size.width * 0.92f, size.height * 0.22f),
            Offset(size.width * 0.18f, size.height * 0.86f),
            Offset(size.width * 0.26f, size.height * 0.80f)
        ).forEach {
            drawCircle(
                color = RedNdaCircuitLine.copy(alpha = 0.9f),
                radius = 4f,
                center = it
            )
        }

        for (row in 0 until 6) {
            for (column in 0 until 4) {
                drawCircle(
                    color = RedNdaCircuitLine.copy(alpha = 0.55f),
                    radius = 3f,
                    center = Offset(
                        x = size.width * 0.04f + column * 28.dp.toPx(),
                        y = size.height * 0.22f + row * 28.dp.toPx()
                    )
                )
            }
        }

        for (row in 0 until 5) {
            for (column in 0 until 3) {
                drawCircle(
                    color = RedNdaCircuitLine.copy(alpha = 0.5f),
                    radius = 3f,
                    center = Offset(
                        x = size.width * 0.93f + column * 24.dp.toPx(),
                        y = size.height * 0.68f + row * 24.dp.toPx()
                    )
                )
            }
        }

        val topRed = Path().apply {
            moveTo(size.width * 0.70f, 0f)
            lineTo(size.width * 0.90f, 0f)
            lineTo(size.width, size.height * 0.12f)
            lineTo(size.width * 0.80f, size.height * 0.12f)
            close()
        }
        drawPath(topRed, RedNdaRed.copy(alpha = 0.94f))

        val topBlack = Path().apply {
            moveTo(size.width * 0.82f, size.height * 0.12f)
            lineTo(size.width, size.height * 0.12f)
            lineTo(size.width, size.height * 0.24f)
            lineTo(size.width * 0.92f, size.height * 0.24f)
            close()
        }
        drawPath(topBlack, RedNdaBlack.copy(alpha = 0.92f))

        val bottomRed = Path().apply {
            moveTo(0f, size.height * 0.88f)
            lineTo(size.width * 0.18f, size.height)
            lineTo(size.width * 0.28f, size.height)
            lineTo(size.width * 0.06f, size.height * 0.82f)
            close()
        }
        drawPath(bottomRed, RedNdaRed.copy(alpha = 0.94f))

        val bottomBlack = Path().apply {
            moveTo(0f, size.height * 0.92f)
            lineTo(size.width * 0.12f, size.height)
            lineTo(size.width * 0.20f, size.height)
            lineTo(size.width * 0.05f, size.height * 0.86f)
            close()
        }
        drawPath(bottomBlack, RedNdaBlack.copy(alpha = 0.92f))

        drawCircle(
            color = Color(0xFF9A9A9A).copy(alpha = 0.06f),
            radius = size.minDimension * 0.24f,
            center = Offset(size.width * 0.78f, size.height * 0.34f)
        )
        drawCircle(
            color = Color(0xFF9A9A9A).copy(alpha = 0.05f),
            radius = size.minDimension * 0.18f,
            center = Offset(size.width * 0.10f, size.height * 0.68f)
        )
    }
}

@Composable
fun BrandLogo(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.logo_innovation_labs),
            contentDescription = "RED Innovation Labs",
            modifier = Modifier.widthIn(max = 340.dp),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(6.dp))
        Canvas(
            modifier = Modifier
                .width(176.dp)
                .height(3.dp)
        ) {
            drawLine(
                color = RedNdaRed,
                start = Offset.Zero,
                end = Offset(size.width, 0f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun WelcomeHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = RedNdaBlack,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                lineHeight = 34.sp
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Canvas(
            modifier = Modifier
                .width(58.dp)
                .height(4.dp)
        ) {
            drawLine(
                color = RedNdaRed,
                start = Offset.Zero,
                end = Offset(size.width, 0f),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = subtitle,
            color = RedNdaDarkGray,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NdaOptionCard(
    iconRes: Int,
    title: androidx.compose.ui.text.AnnotatedString,
    description: String,
    buttonText: String,
    buttonIconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = RedNdaWhite)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            CardCornerAccent()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .background(RedNdaLightGray.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = title,
                    color = RedNdaBlack,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = (-0.4).sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Canvas(
                    modifier = Modifier
                        .width(66.dp)
                        .height(3.dp)
                ) {
                    drawLine(
                        color = RedNdaRed,
                        start = Offset.Zero,
                        end = Offset(size.width, 0f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = description,
                    color = RedNdaDarkGray,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                RedPrimaryButton(
                    text = buttonText,
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    trailingContent = {
                        Icon(
                            painter = painterResource(id = buttonIconRes),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CardCornerAccent() {
    Canvas(
        modifier = Modifier
            .size(width = 44.dp, height = 44.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path = path, color = RedNdaRed)
    }
}

@Composable
fun FooterBrand(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DecorativeDivider(modifier = Modifier.width(88.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_red_automation_black),
            contentDescription = "RED Automation",
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .widthIn(max = 220.dp),
            contentScale = ContentScale.FillWidth
        )
        DecorativeDivider(modifier = Modifier.width(88.dp))
    }
}

@Composable
private fun DecorativeDivider(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.height(12.dp)
    ) {
        val centerY = size.height / 2f
        drawLine(
            color = RedNdaBorderGray,
            start = Offset(0f, centerY),
            end = Offset(size.width * 0.78f, centerY),
            strokeWidth = 1.6f
        )
        drawRect(
            color = RedNdaBlack,
            topLeft = Offset(size.width * 0.82f, centerY - 2f),
            size = Size(8f, 4f)
        )
        drawRect(
            color = RedNdaRedDark,
            topLeft = Offset(size.width * 0.88f, centerY - 2f),
            size = Size(12f, 4f)
        )
    }
}

@Preview(
    name = "NDA Home Portrait",
    showBackground = true,
    backgroundColor = 0xFFFAFAFA,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun NdaHomeScreenPreview() {
    RedNdaTheme {
        NdaHomeScreen(
            uiState = HomeUiState(),
            onProceedClick = {},
            onScanClick = {}
        )
    }
}
