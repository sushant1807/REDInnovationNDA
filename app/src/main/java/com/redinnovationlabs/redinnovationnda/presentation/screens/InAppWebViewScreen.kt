package com.redinnovationlabs.redinnovationnda.presentation.screens

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import android.webkit.CookieManager
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.redinnovationlabs.redinnovationnda.data.constants.DocuSignConstants
import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.presentation.components.RedPrimaryButton
import com.redinnovationlabs.redinnovationnda.presentation.components.IdleTimeoutController
import com.redinnovationlabs.redinnovationnda.presentation.components.IdleTimeoutOverlay
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBlack
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaBorderGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaLightGray
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaRed
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaWhite
import com.redinnovationlabs.redinnovationnda.presentation.components.rememberIdleTimeoutController
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.WebViewViewModel
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model.WebViewUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppWebViewScreen(
    viewModel: WebViewViewModel,
    onBack: () -> Unit,
    onTimeoutHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    val webViewSupport = remember { evaluateWebViewSupport() }
    val resolvedErrorMessage = state.errorMessage ?: webViewSupport.unsupportedReason
    val idleTimeoutController = rememberIdleTimeoutController(onTimeout = onTimeoutHome)

    BackHandler {
        val webView = webViewRef
        idleTimeoutController.registerInteraction()
        if (webView?.canGoBack() == true) {
            webView.goBack()
        } else {
            onBack()
        }
    }

    InAppWebViewScreenContent(
        state = state.copy(
            isLoading = if (resolvedErrorMessage != null) false else state.isLoading,
            errorMessage = resolvedErrorMessage
        ),
        idleTimeoutController = idleTimeoutController,
        onBack = {
            val webView = webViewRef
            idleTimeoutController.registerInteraction()
            if (webView?.canGoBack() == true) {
                webView.goBack()
            } else {
                onBack()
            }
        },
        onRetry = viewModel::clearError,
        onOpenSecureForm = {
            idleTimeoutController.registerInteraction()
            openNdaFormExternally(
                context = context,
                url = state.formLink.url
            )
        },
        onTimeoutHome = onTimeoutHome,
        showRetry = webViewSupport.unsupportedReason == null,
        modifier = modifier,
        webViewSlot = {
            if (webViewSupport.isSupported) {
                RedWebView(
                    url = state.formLink.url,
                    onCreated = { webViewRef = it },
                    onUserInteraction = idleTimeoutController::registerInteraction,
                    onPageStarted = {
                        viewModel.markLoading(true)
                        viewModel.updateProgress(8)
                    },
                    onPageFinished = {
                        viewModel.updateProgress(100)
                        viewModel.markLoading(false)
                    },
                    onProgressChanged = viewModel::updateProgress,
                    onPageError = viewModel::setError,
                    onEmbeddedUnsupported = viewModel::setError
                )
            }
        }
    )
}

@Composable
private fun InAppWebViewScreenContent(
    state: WebViewUiState,
    idleTimeoutController: IdleTimeoutController,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onOpenSecureForm: () -> Unit,
    modifier: Modifier = Modifier,
    onTimeoutHome: () -> Unit,
    showRetry: Boolean = true,
    webViewSlot: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = RedNdaWhite
    ) {
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
            WebViewBackdrop()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                RedTopBar(
                    title = state.screenTitle,
                    onBack = onBack
                )

                if (state.isLoading && state.errorMessage == null) {
                    LinearProgressIndicator(
                        progress = { (state.loadingProgress.coerceIn(0, 100) / 100f) },
                        modifier = Modifier.fillMaxWidth(),
                        color = RedNdaRed,
                        trackColor = RedNdaBorderGray.copy(alpha = 0.35f)
                    )
                } else {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    if (state.errorMessage == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = RedNdaWhite.copy(alpha = 0.92f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            webViewSlot()

                            if (state.isLoading) {
                                LoadingOverlay(progress = state.loadingProgress)
                            }
                        }
                    } else {
                        WebViewErrorPanel(
                            message = state.errorMessage,
                            showRetry = showRetry,
                            retryText = state.retryButtonText,
                            openSecureFormText = "OPEN SECURE FORM",
                            backText = state.backButtonText,
                            onRetry = onRetry,
                            onOpenSecureForm = onOpenSecureForm,
                            onBack = onBack
                        )
                    }
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

@Composable
private fun RedTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(RedNdaWhite.copy(alpha = 0.95f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(RedNdaLightGray.copy(alpha = 0.78f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = RedNdaBlack
            )
        }

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = RedNdaBlack,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.4.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingOverlay(
    progress: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = RedNdaWhite.copy(alpha = 0.94f),
            shape = RoundedCornerShape(18.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = RedNdaRed)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading NDA form ${progress.coerceIn(0, 100)}%",
                    color = RedNdaBlack,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun RedWebView(
    url: String,
    onCreated: (WebView) -> Unit,
    onUserInteraction: () -> Unit,
    onPageStarted: () -> Unit,
    onPageFinished: () -> Unit,
    onProgressChanged: (Int) -> Unit,
    onPageError: (String) -> Unit,
    onEmbeddedUnsupported: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    val loadTimeout = remember(url) {
        Runnable {
            val currentWebView = webViewInstance
            if (
                currentWebView != null &&
                currentWebView.progress == 0 &&
                currentWebView.url.isNullOrBlank()
            ) {
                onPageError("The NDA form is taking too long to load in WebView. Please try again.")
            }
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            WebView(context).apply {
                configureForDocuSign()
                onCreated(this)
                webViewInstance = this
                setOnTouchListener { webView, motionEvent ->
                    if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                        onUserInteraction()
                    } else if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
                        webView.performClick()
                    }
                    false
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onProgressChanged(newProgress)
                    }

                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        val message = consoleMessage?.message().orEmpty()
                        if (
                            message.contains("Something went wrong initializing 1ds-app", ignoreCase = true) ||
                            message.contains("SyntaxError: Unexpected token", ignoreCase = true)
                        ) {
                            mainHandler.removeCallbacks(loadTimeout)
                            onEmbeddedUnsupported(
                                "This device's Android WebView is not compatible with the DocuSign form. Open the secure form in a browser tab or update Android System WebView/Chrome."
                            )
                            return true
                        }
                        return super.onConsoleMessage(consoleMessage)
                    }

                    override fun onCreateWindow(
                        view: WebView?,
                        isDialog: Boolean,
                        isUserGesture: Boolean,
                        resultMsg: Message?
                    ): Boolean {
                        val transport = resultMsg?.obj as? WebView.WebViewTransport ?: return false
                        transport.webView = view
                        resultMsg.sendToTarget()
                        return true
                    }
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onPageStarted()
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val targetUrl = request?.url?.toString() ?: return false
                        val scheme = request.url?.scheme.orEmpty()

                        return when {
                            scheme.equals("http", ignoreCase = true) ||
                                scheme.equals("https", ignoreCase = true) -> false
                            else -> {
                                onPageError(
                                    "This DocuSign step requested an unsupported link: $targetUrl"
                                )
                                true
                            }
                        }
                    }

                    override fun onPageCommitVisible(view: WebView?, url: String?) {
                        onProgressChanged(80)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        mainHandler.removeCallbacks(loadTimeout)
                        CookieManager.getInstance().flush()
                        onPageFinished()
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        if (request?.isForMainFrame == true) {
                            mainHandler.removeCallbacks(loadTimeout)
                            onPageError(
                                error?.description?.toString()
                                    ?: "Unable to load the NDA form."
                            )
                        }
                    }

                    override fun onReceivedHttpError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        errorResponse: android.webkit.WebResourceResponse?
                    ) {
                        if (request?.isForMainFrame == true) {
                            mainHandler.removeCallbacks(loadTimeout)
                            onPageError(
                                "The NDA form returned an HTTP ${errorResponse?.statusCode ?: "error"} response."
                            )
                        }
                    }
                }
                mainHandler.postDelayed(loadTimeout, 15000)
                loadUrl(url)
            }
        },
        update = { webView ->
            onCreated(webView)
            webViewInstance = webView
            if (webView.url.isNullOrEmpty()) {
                mainHandler.removeCallbacks(loadTimeout)
                mainHandler.postDelayed(loadTimeout, 15000)
                webView.loadUrl(url)
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            mainHandler.removeCallbacks(loadTimeout)
            webViewInstance?.stopLoading()
            webViewInstance?.webChromeClient = null
            webViewInstance?.destroy()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureForDocuSign() {
    val cookieManager = CookieManager.getInstance()
    cookieManager.setAcceptCookie(true)
    cookieManager.setAcceptThirdPartyCookies(this, true)

    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.loadsImagesAutomatically = true
    settings.loadWithOverviewMode = true
    settings.useWideViewPort = true
    settings.builtInZoomControls = false
    settings.displayZoomControls = false
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.setSupportMultipleWindows(true)
    settings.allowFileAccess = true
    settings.allowContentAccess = true
    settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
    settings.cacheMode = WebSettings.LOAD_DEFAULT
    settings.mediaPlaybackRequiresUserGesture = false

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        settings.safeBrowsingEnabled = true
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        settings.isAlgorithmicDarkeningAllowed = false
    }

    isFocusable = true
    isFocusableInTouchMode = true
}

@Composable
private fun WebViewBackdrop(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val majorGrid = 36.dp.toPx()
        val minorGrid = 12.dp.toPx()
        val majorColor = RedNdaBorderGray.copy(alpha = 0.20f)
        val minorColor = RedNdaBorderGray.copy(alpha = 0.10f)
        val scratchColor = Color(0xFF777777).copy(alpha = 0.14f)
        val hazeColor = Color(0xFF9A9A9A).copy(alpha = 0.06f)

        var x = 0f
        while (x <= size.width) {
            drawLine(majorColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
            x += majorGrid
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(majorColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
            y += majorGrid
        }

        var mx = 0f
        while (mx <= size.width) {
            drawLine(minorColor, Offset(mx, 0f), Offset(mx, size.height), strokeWidth = 0.6f)
            mx += minorGrid
        }

        var my = 0f
        while (my <= size.height) {
            drawLine(minorColor, Offset(0f, my), Offset(size.width, my), strokeWidth = 0.6f)
            my += minorGrid
        }

        val scratches = listOf(
            Pair(Offset(size.width * 0.06f, size.height * 0.08f), Offset(size.width * 0.28f, size.height * 0.14f)),
            Pair(Offset(size.width * 0.66f, size.height * 0.12f), Offset(size.width * 0.90f, size.height * 0.22f)),
            Pair(Offset(size.width * 0.18f, size.height * 0.76f), Offset(size.width * 0.42f, size.height * 0.82f)),
            Pair(Offset(size.width * 0.74f, size.height * 0.62f), Offset(size.width * 0.96f, size.height * 0.70f))
        )

        scratches.forEachIndexed { index, (start, end) ->
            drawLine(
                color = scratchColor.copy(alpha = if (index % 2 == 0) 0.15f else 0.10f),
                start = start,
                end = end,
                strokeWidth = if (index % 2 == 0) 2f else 1.2f,
                cap = StrokeCap.Round
            )
        }

        drawCircle(
            color = hazeColor,
            radius = size.minDimension * 0.22f,
            center = Offset(size.width * 0.14f, size.height * 0.18f)
        )
        drawCircle(
            color = hazeColor,
            radius = size.minDimension * 0.18f,
            center = Offset(size.width * 0.82f, size.height * 0.18f)
        )
    }
}

@Composable
private fun WebViewErrorPanel(
    message: String,
    showRetry: Boolean,
    retryText: String,
    openSecureFormText: String,
    backText: String,
    onRetry: () -> Unit,
    onOpenSecureForm: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(360.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ERROR",
                color = RedNdaBlack,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Canvas(
                modifier = Modifier
                    .width(130.dp)
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = RedNdaBlack,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (showRetry) {
                RedPrimaryButton(
                    text = retryText,
                    onClick = onRetry
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            RedPrimaryButton(
                text = openSecureFormText,
                onClick = onOpenSecureForm
            )
            Spacer(modifier = Modifier.height(12.dp))
            RedPrimaryButton(
                text = backText,
                onClick = onBack
            )
        }
    }
}

@Preview(
    name = "WebView Error Preview",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 1280,
    heightDp = 800
)
@Composable
private fun InAppWebViewScreenPreview() {
    val previewState = WebViewUiState(
        formLink = NdaFormLink(
            url = DocuSignConstants.NDA_WEBFORM_URL,
            title = "Complete NDA",
            shareSubject = "RED NDA App",
            shareMessagePrefix = "Use this secure DocuSign WebForm link to complete the NDA:\n"
        ),
        isLoading = false,
        errorMessage = "Unable to connect to the DocuSign WebForm. Please check the connection and try again."
    )

    RedNdaTheme {
        InAppWebViewScreenContent(
            state = previewState,
            idleTimeoutController = rememberIdleTimeoutController(onTimeout = {}),
            onBack = {},
            onRetry = {},
            onOpenSecureForm = {},
            onTimeoutHome = {},
            showRetry = true,
            webViewSlot = {}
        )
    }
}

private data class WebViewSupport(
    val isSupported: Boolean,
    val unsupportedReason: String? = null
)

private fun evaluateWebViewSupport(): WebViewSupport {
    //noinspection WebViewApiAvailability
    val webViewPackage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WebView.getCurrentWebViewPackage()
    } else {
        null
    }
    val versionName = webViewPackage?.versionName.orEmpty()
    val majorVersion = versionName.substringBefore('.').toIntOrNull()

    if (majorVersion == null) {
        return WebViewSupport(
            isSupported = false,
            unsupportedReason = "Android System WebView is unavailable on this device. Open the secure form in a browser tab."
        )
    }

    return if (majorVersion < 100) {
        WebViewSupport(
            isSupported = false,
            unsupportedReason = "This device is using Android System WebView $majorVersion, which is too old for the DocuSign form. Open the secure form in a browser tab or update Android System WebView/Chrome."
        )
    } else {
        WebViewSupport(isSupported = true)
    }
}

private fun openNdaFormExternally(
    context: android.content.Context,
    url: String
) {
    val uri = url.toUri()

    runCatching {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(context, uri)
    }.recoverCatching {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }.getOrElse { error ->
        if (error !is ActivityNotFoundException) {
            throw error
        }
    }
}
