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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.redinnovationlabs.redinnovationnda.presentation.components.DiagonalAccent
import com.redinnovationlabs.redinnovationnda.presentation.components.DiagonalAccentPosition
import com.redinnovationlabs.redinnovationnda.presentation.components.GridBackground
import com.redinnovationlabs.redinnovationnda.presentation.components.RedPrimaryButton
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaDimens
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed

@Composable
fun SuccessScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        GridBackground()
        DiagonalAccent(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 0.dp, end = 0.dp),
            position = DiagonalAccentPosition.BottomStart
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = RedNdaDimens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = RedNdaRed,
                modifier = Modifier.padding(bottom = 18.dp)
            )
            Text(
                text = "THANK YOU!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = RedNdaBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your NDA has been submitted successfully.",
                style = MaterialTheme.typography.bodyLarge,
                color = RedNdaBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You may now close this screen.",
                style = MaterialTheme.typography.bodyLarge,
                color = RedNdaBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            RedPrimaryButton(
                text = "DONE",
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 52.dp)
            )
        }
    }
}
