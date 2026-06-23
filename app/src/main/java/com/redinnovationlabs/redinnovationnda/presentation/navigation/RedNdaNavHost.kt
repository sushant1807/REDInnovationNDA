package com.redinnovationlabs.redinnovationnda.presentation.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.core.net.toUri
import com.redinnovationlabs.redinnovationnda.data.constants.DocuSignConstants
import com.redinnovationlabs.redinnovationnda.presentation.screens.ErrorScreen
import com.redinnovationlabs.redinnovationnda.presentation.screens.HomeScreen
import com.redinnovationlabs.redinnovationnda.presentation.screens.InAppWebViewScreen
import com.redinnovationlabs.redinnovationnda.presentation.screens.QrCodeScreen
import com.redinnovationlabs.redinnovationnda.presentation.screens.SplashScreen
import com.redinnovationlabs.redinnovationnda.presentation.screens.SuccessScreen
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.HomeViewModel
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.QrCodeViewModel
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.RedNdaViewModelFactory
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.WebViewViewModel

private const val EXTERNAL_NDA_TIMEOUT_MILLIS = 1 * 60 * 1000L

@Composable
fun RedNdaNavHost(
    navController: NavHostController,
    viewModelFactory: RedNdaViewModelFactory
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var externalNdaLaunchAt by remember { mutableLongStateOf(0L) }
    var isAwaitingExternalNdaReturn by remember { mutableStateOf(false) }

    DisposableEffect(
        lifecycleOwner,
        navController,
        isAwaitingExternalNdaReturn,
        externalNdaLaunchAt
    ) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && isAwaitingExternalNdaReturn) {
                val elapsed = SystemClock.elapsedRealtime() - externalNdaLaunchAt
                if (elapsed >= EXTERNAL_NDA_TIMEOUT_MILLIS) {
                    navController.popBackStack(RedNdaRoute.Home.route, inclusive = false)
                }
                isAwaitingExternalNdaReturn = false
                externalNdaLaunchAt = 0L
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    NavHost(
        navController = navController,
        startDestination = RedNdaRoute.Splash.route
    ) {
        composable(RedNdaRoute.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(RedNdaRoute.Home.route) {
                        popUpTo(RedNdaRoute.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(RedNdaRoute.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState.value,
                onProceedClick = {
                    val didLaunch = openNdaFormExternally(
                        context = context,
                        url = DocuSignConstants.NDA_WEBFORM_URL
                    )
                    if (didLaunch) {
                        externalNdaLaunchAt = SystemClock.elapsedRealtime()
                        isAwaitingExternalNdaReturn = true
                    }
                },
                onScanClick = { navController.navigate(RedNdaRoute.QrCode.route) }
            )
        }
        composable(RedNdaRoute.WebView.route) {
            val viewModel: WebViewViewModel = viewModel(factory = viewModelFactory)
            InAppWebViewScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onTimeoutHome = {
                    navController.popBackStack(RedNdaRoute.Home.route, inclusive = false)
                }
            )
        }
        composable(RedNdaRoute.QrCode.route) {
            val viewModel: QrCodeViewModel = viewModel(factory = viewModelFactory)
            QrCodeScreen(
                uiState = viewModel.uiState,
                onBack = { navController.popBackStack() },
                onTimeoutHome = {
                    navController.popBackStack(RedNdaRoute.Home.route, inclusive = false)
                }
            )
        }
        composable(RedNdaRoute.Success.route) {
            SuccessScreen(
                onDone = { navController.popBackStack(RedNdaRoute.Home.route, inclusive = false) }
            )
        }
        composable(RedNdaRoute.Error.route) {
            ErrorScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun openNdaFormExternally(
    context: Context,
    url: String
): Boolean {
    val uri = url.toUri()

    return runCatching {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(context, uri)
        true
    }.recoverCatching {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        true
    }.getOrElse { error ->
        if (error !is ActivityNotFoundException) {
            throw error
        }
        false
    }
}
