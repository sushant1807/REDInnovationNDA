package com.redinnovationlabs.redinnovationnda.presentation.screens

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.data.constants.DocuSignConstants
import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.presentation.components.QrCodeImage
import com.redinnovationlabs.redinnovationnda.presentation.components.IdleTimeoutOverlay
import com.redinnovationlabs.redinnovationnda.presentation.components.RedPrimaryButton
import com.redinnovationlabs.redinnovationnda.presentation.components.rememberIdleTimeoutController
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaWhite
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model.QrCodeUiState

@Composable
fun QrCodeScreen(
    uiState: QrCodeUiState,
    onBack: () -> Unit,
    onTimeoutHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val idleTimeoutController = rememberIdleTimeoutController(onTimeout = onTimeoutHome)
    val chooserTitle = stringResource(R.string.share_nda_link_chooser_title)
    var hasHandledBack by remember { mutableStateOf(false) }

    QrCodeScreenContent(
        state = uiState,
        idleTimeoutController = idleTimeoutController,
        onBack = {
            if (hasHandledBack) return@QrCodeScreenContent
            hasHandledBack = true
            idleTimeoutController.registerInteraction()
            onBack()
        },
        onShare = {
            idleTimeoutController.registerInteraction()
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, uiState.formLink.shareSubject)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${uiState.formLink.shareMessagePrefix}${uiState.formLink.url}"
                )
            }
            context.startActivity(Intent.createChooser(sendIntent, chooserTitle))
        },
        onTimeoutHome = onTimeoutHome,
        modifier = modifier
    )
}

@Composable
private fun QrCodeScreenContent(
    state: QrCodeUiState,
    idleTimeoutController: com.redinnovationlabs.redinnovationnda.presentation.components.IdleTimeoutController,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onTimeoutHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = RedNdaWhite
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val isLargePortrait = maxWidth >= 600.dp
            val horizontalPadding = if (isLargePortrait) 32.dp else 22.dp
            val titleSize = if (isLargePortrait) 40.sp else 34.sp
            val contentMaxWidth = if (isLargePortrait) 480.dp else 380.dp
            val qrCardSize = if (isLargePortrait) 340.dp else 286.dp
            val qrImageSize = if (isLargePortrait) 236.dp else 198.dp

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter {
                        idleTimeoutController.registerInteraction()
                        false
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFDFDFD),
                                RedNdaLightGray,
                                Color(0xFFF7F7F7)
                            )
                        )
                    )
            ) {
                QrBackdrop()
                QrTopAccent(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 34.dp, end = 26.dp)
                )
                QrBottomAccent(
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
                QrDotPattern(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 28.dp, bottom = 24.dp)
                )

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = horizontalPadding - 8.dp, top = 42.dp)
                        .background(RedNdaWhite.copy(alpha = 0.92f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_back),
                        tint = RedNdaBlack
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.widthIn(max = contentMaxWidth),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.qr_title),
                            color = RedNdaBlack,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = titleSize,
                                letterSpacing = (-0.8).sp
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TitleUnderline()
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = stringResource(R.string.qr_subtitle),
                            color = RedNdaBlack,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = if (isLargePortrait) 18.sp else 16.sp,
                                lineHeight = if (isLargePortrait) 26.sp else 22.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        QrFramedCard(
                            modifier = Modifier.size(qrCardSize),
                            qrContent = {
                                QrCodeImage(
                                    content = state.formLink.url,
                                    modifier = Modifier.size(qrImageSize)
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        RedPrimaryButton(
                            text = stringResource(R.string.qr_share_button),
                            onClick = onShare,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = if (isLargePortrait) 52.dp else 16.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                IdleTimeoutOverlay(
                    controller = idleTimeoutController,
                    onStayActive = idleTimeoutController::registerInteraction,
                    onGoHome = onTimeoutHome
                )
            }
        }
    }
}

@Composable
private fun QrBackdrop(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val majorGrid = 36.dp.toPx()
        val minorGrid = 12.dp.toPx()
        val majorColor = RedNdaBorderGray.copy(alpha = 0.22f)
        val minorColor = RedNdaBorderGray.copy(alpha = 0.10f)
        val scratchColor = Color(0xFF777777).copy(alpha = 0.15f)
        val hazeColor = Color(0xFF9A9A9A).copy(alpha = 0.07f)

        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = majorColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += majorGrid
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = majorColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += majorGrid
        }

        var mx = 0f
        while (mx <= size.width) {
            drawLine(
                color = minorColor,
                start = Offset(mx, 0f),
                end = Offset(mx, size.height),
                strokeWidth = 0.6f
            )
            mx += minorGrid
        }

        var my = 0f
        while (my <= size.height) {
            drawLine(
                color = minorColor,
                start = Offset(0f, my),
                end = Offset(size.width, my),
                strokeWidth = 0.6f
            )
            my += minorGrid
        }

        val scratches = listOf(
            Pair(
                Offset(size.width * 0.08f, size.height * 0.10f),
                Offset(size.width * 0.28f, size.height * 0.18f)
            ),
            Pair(
                Offset(size.width * 0.62f, size.height * 0.08f),
                Offset(size.width * 0.90f, size.height * 0.18f)
            ),
            Pair(
                Offset(size.width * 0.14f, size.height * 0.66f),
                Offset(size.width * 0.34f, size.height * 0.72f)
            ),
            Pair(
                Offset(size.width * 0.66f, size.height * 0.74f),
                Offset(size.width * 0.90f, size.height * 0.82f)
            ),
            Pair(
                Offset(size.width * 0.76f, size.height * 0.32f),
                Offset(size.width * 0.96f, size.height * 0.44f)
            )
        )

        scratches.forEachIndexed { index, (start, end) ->
            drawLine(
                color = scratchColor.copy(alpha = if (index % 2 == 0) 0.16f else 0.11f),
                start = start,
                end = end,
                strokeWidth = if (index % 3 == 0) 2f else 1.2f,
                cap = StrokeCap.Round
            )
        }

        drawCircle(
            color = hazeColor,
            radius = size.minDimension * 0.20f,
            center = Offset(size.width * 0.18f, size.height * 0.20f)
        )
        drawCircle(
            color = hazeColor,
            radius = size.minDimension * 0.18f,
            center = Offset(size.width * 0.82f, size.height * 0.16f)
        )
    }
}

@Composable
private fun TitleUnderline(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .width(160.dp)
            .height(6.dp)
    ) {
        drawLine(
            color = RedNdaRed,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = 2.4f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun QrFramedCard(
    qrContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = RedNdaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = RedNdaBorderGray.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(24.dp)
                    )
            )
            QrCornerFrame()
            qrContent()
        }
    }
}

@Composable
private fun QrCornerFrame(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val inset = 18.dp.toPx()
        val arm = 34.dp.toPx()
        val strokeWidth = 8.dp.toPx()
        val color = RedNdaRed

        drawLine(
            color,
            Offset(inset, inset + arm),
            Offset(inset, inset),
            strokeWidth,
            cap = StrokeCap.Square
        )
        drawLine(
            color,
            Offset(inset, inset),
            Offset(inset + arm, inset),
            strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color,
            Offset(size.width - inset - arm, inset),
            Offset(size.width - inset, inset),
            strokeWidth,
            cap = StrokeCap.Square
        )
        drawLine(
            color,
            Offset(size.width - inset, inset),
            Offset(size.width - inset, inset + arm),
            strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color,
            Offset(inset, size.height - inset - arm),
            Offset(inset, size.height - inset),
            strokeWidth,
            cap = StrokeCap.Square
        )
        drawLine(
            color,
            Offset(inset, size.height - inset),
            Offset(inset + arm, size.height - inset),
            strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color,
            Offset(size.width - inset - arm, size.height - inset),
            Offset(size.width - inset, size.height - inset),
            strokeWidth,
            cap = StrokeCap.Square
        )
        drawLine(
            color,
            Offset(size.width - inset, size.height - inset - arm),
            Offset(size.width - inset, size.height - inset),
            strokeWidth,
            cap = StrokeCap.Square
        )
    }
}

@Composable
private fun QrTopAccent(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .width(72.dp)
            .height(112.dp)
    ) {
        val path = Path().apply {
            moveTo(size.width * 0.56f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width * 0.40f, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path = path, color = RedNdaRed)
    }
}

@Composable
private fun QrBottomAccent(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .width(86.dp)
            .height(122.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width * 0.58f, 0f)
            lineTo(size.width, size.height)
            lineTo(size.width * 0.40f, size.height)
            close()
        }
        drawPath(path = path, color = RedNdaRed)
    }
}

@Composable
private fun QrDotPattern(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.size(82.dp)
    ) {
        val spacing = 12.dp.toPx()
        val radius = 1.8.dp.toPx()
        var row = 0
        var y = spacing / 2f
        while (y <= size.height) {
            var column = 0
            var x = spacing / 2f
            while (x <= size.width) {
                if ((row + column) % 2 == 0) {
                    drawCircle(
                        color = RedNdaBlack.copy(alpha = 0.72f),
                        radius = radius,
                        center = Offset(x, y)
                    )
                }
                x += spacing
                column++
            }
            y += spacing
            row++
        }
    }
}

@Preview(
    name = "QR Tablet Portrait",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 800,
    heightDp = 1280
)
@Composable
private fun QrCodeScreenPreview() {
    val previewState = QrCodeUiState(
        formLink = NdaFormLink(
            url = DocuSignConstants.NDA_WEBFORM_URL,
            title = stringResource(R.string.nda_webform_title),
            shareSubject = stringResource(R.string.nda_share_subject),
            shareMessagePrefix = stringResource(R.string.nda_share_message_prefix)
        )
    )

    RedNdaTheme {
        QrCodeScreenContent(
            state = previewState,
            idleTimeoutController = rememberIdleTimeoutController(onTimeout = {}),
            onBack = {},
            onShare = {},
            onTimeoutHome = {}
        )
    }
}
