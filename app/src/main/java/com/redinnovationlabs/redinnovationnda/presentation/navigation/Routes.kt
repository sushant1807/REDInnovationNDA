package com.redinnovationlabs.redinnovationnda.presentation.navigation

sealed class RedNdaRoute(val route: String) {
    data object Splash : RedNdaRoute("splash")
    data object Home : RedNdaRoute("home")
    data object WebView : RedNdaRoute("webview")
    data object QrCode : RedNdaRoute("qr_code")
    data object Success : RedNdaRoute("success")
    data object Error : RedNdaRoute("error")
}
