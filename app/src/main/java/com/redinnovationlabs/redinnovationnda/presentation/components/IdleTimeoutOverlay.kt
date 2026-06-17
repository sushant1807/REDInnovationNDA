package com.redinnovationlabs.redinnovationnda.presentation.components

import android.os.SystemClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaWhite
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private const val DEFAULT_IDLE_TIMEOUT_MILLIS = 1 * 60 * 1000L
private const val DEFAULT_WARNING_DURATION_SECONDS = 60

@Stable
class IdleTimeoutController internal constructor(
    internal val warningDurationSeconds: Int
) {
    internal var lastInteractionAt by mutableLongStateOf(SystemClock.elapsedRealtime())
    internal var isWarningVisible by mutableStateOf(false)
    internal var remainingSeconds by mutableIntStateOf(warningDurationSeconds)

    fun registerInteraction() {
        lastInteractionAt = SystemClock.elapsedRealtime()
        isWarningVisible = false
        remainingSeconds = warningDurationSeconds
    }
}

@Composable
fun rememberIdleTimeoutController(
    idleTimeoutMillis: Long = DEFAULT_IDLE_TIMEOUT_MILLIS,
    warningDurationSeconds: Int = DEFAULT_WARNING_DURATION_SECONDS,
    onTimeout: () -> Unit
): IdleTimeoutController {
    val controller = remember(idleTimeoutMillis, warningDurationSeconds) {
        IdleTimeoutController(
            warningDurationSeconds = warningDurationSeconds
        )
    }

    LaunchedEffect(controller.lastInteractionAt, controller.isWarningVisible) {
        if (!controller.isWarningVisible) {
            delay(idleTimeoutMillis.milliseconds)
            val hasReachedIdleLimit =
                SystemClock.elapsedRealtime() - controller.lastInteractionAt >= idleTimeoutMillis
            if (hasReachedIdleLimit) {
                controller.isWarningVisible = true
                controller.remainingSeconds = controller.warningDurationSeconds
            }
        }
    }

    LaunchedEffect(controller.isWarningVisible) {
        if (controller.isWarningVisible) {
            while (controller.isWarningVisible && controller.remainingSeconds > 0) {
                delay(1000.milliseconds)
                controller.remainingSeconds -= 1
            }

            if (controller.isWarningVisible && controller.remainingSeconds <= 0) {
                onTimeout()
            }
        }
    }

    return controller
}

@Composable
fun IdleTimeoutOverlay(
    controller: IdleTimeoutController,
    onStayActive: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = controller.remainingSeconds / controller.warningDurationSeconds.toFloat(),
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "idleTimeoutProgress"
    )

    AnimatedVisibility(
        visible = controller.isWarningVisible,
        enter = fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.96f),
        exit = fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 0.98f),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xB5121217)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                shape = RoundedCornerShape(28.dp),
                color = RedNdaWhite.copy(alpha = 0.97f),
                shadowElevation = 22.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    RedNdaWhite,
                                    Color(0xFFF9F9F9),
                                    RedNdaLightGray.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(126.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = RedNdaBorderGray.copy(alpha = 0.32f),
                                radius = size.minDimension / 2f,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12.dp.toPx())
                            )
                            drawArc(
                                color = RedNdaRed,
                                startAngle = -90f,
                                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                                useCenter = false,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 12.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                            drawLine(
                                color = RedNdaRed.copy(alpha = 0.28f),
                                start = Offset(size.width * 0.22f, size.height * 0.82f),
                                end = Offset(size.width * 0.78f, size.height * 0.82f),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = controller.remainingSeconds.coerceAtLeast(0).toString(),
                                color = RedNdaBlack,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 34.sp
                                )
                            )
                            Text(
                                text = "SECONDS",
                                color = RedNdaRed,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "SESSION ENDING SOON",
                        color = RedNdaBlack,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.6.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Canvas(
                        modifier = Modifier
                            .width(96.dp)
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

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "For privacy, this screen will return to Home after 1 minute of no activity.",
                        color = RedNdaBlack.copy(alpha = 0.84f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 23.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    RedPrimaryButton(
                        text = "STAY ON THIS SCREEN",
                        onClick = onStayActive
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Prefer to leave now?",
                            color = RedNdaBlack.copy(alpha = 0.64f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RETURN HOME",
                            color = RedNdaRed,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            modifier = Modifier
                                .clickable(onClick = onGoHome)
                        )
                    }
                }
            }
        }
    }
}
