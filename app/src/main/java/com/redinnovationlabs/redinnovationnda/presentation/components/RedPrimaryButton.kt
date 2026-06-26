package com.redinnovationlabs.redinnovationnda.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRedDark
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaWhite

@Composable
fun RedPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minHeight: Dp = 56.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "redButtonScale"
    )
    val buttonElevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 12.dp,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "redButtonElevation"
    )
    val iconOffset by animateFloatAsState(
        targetValue = if (isPressed) 10f else 0f,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "redButtonIconOffset"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = buttonScale
                scaleY = buttonScale
            },
        shape = RoundedCornerShape(14.dp),
        shadowElevation = buttonElevation,
        color = androidx.compose.ui.graphics.Color.Transparent
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPressed) RedNdaRedDark else RedNdaRed,
                contentColor = RedNdaWhite
            ),
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.ExtraBold,
                fontSize = fontSize,
                modifier = Modifier.weight(1f, fill = false)
            )
            if (trailingContent != null) {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier.graphicsLayer {
                        translationX = iconOffset
                    },
                    contentAlignment = Alignment.Center
                ) {
                    trailingContent()
                }
            }
        }
    }
}
