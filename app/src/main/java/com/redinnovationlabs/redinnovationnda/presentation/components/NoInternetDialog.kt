package com.redinnovationlabs.redinnovationnda.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaDarkGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaWhite

@Composable
fun NoInternetDialog(
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        containerColor = RedNdaWhite,
        shape = RoundedCornerShape(28.dp),
        modifier = modifier,
        text = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = RedNdaWhite,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = RedNdaRed,
                        trackColor = RedNdaLightGray,
                        strokeWidth = 4.dp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = stringResource(R.string.offline_title),
                        color = RedNdaBlack,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.offline_message),
                        color = RedNdaDarkGray,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.offline_waiting),
                        color = RedNdaBorderGray.copy(alpha = 0.95f),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
@Composable
private fun NoInternetDialogPreview() {
    RedNdaTheme {
        NoInternetDialog()
    }
}
