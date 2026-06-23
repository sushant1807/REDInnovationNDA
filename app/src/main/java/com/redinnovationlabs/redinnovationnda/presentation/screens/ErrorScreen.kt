package com.redinnovationlabs.redinnovationnda.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.presentation.components.DiagonalAccent
import com.redinnovationlabs.redinnovationnda.presentation.components.DiagonalAccentPosition
import com.redinnovationlabs.redinnovationnda.presentation.components.GridBackground
import com.redinnovationlabs.redinnovationnda.presentation.components.RedPrimaryButton
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaDimens
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed

@Composable
fun ErrorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null,
    onRetry: (() -> Unit)? = null,
) {
    val resolvedMessage = message ?: stringResource(R.string.generic_error_message)

    Box(modifier = modifier.fillMaxSize()) {
        GridBackground()
        DiagonalAccent(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 18.dp),
            position = DiagonalAccentPosition.TopEnd
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = RedNdaDimens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = RedNdaRed,
                modifier = Modifier.padding(bottom = 18.dp)
            )
            Text(
                text = stringResource(R.string.generic_error_title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = RedNdaBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = resolvedMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = RedNdaBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            if (onRetry != null) {
                RedPrimaryButton(
                    text = stringResource(R.string.generic_retry_button),
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 52.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            RedPrimaryButton(
                text = stringResource(R.string.generic_go_back_button),
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 52.dp)
            )
        }
    }
}
