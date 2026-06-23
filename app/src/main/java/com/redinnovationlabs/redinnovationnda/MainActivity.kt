package com.redinnovationlabs.redinnovationnda

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.redinnovationlabs.redinnovationnda.data.di.RedNdaContainer
import com.redinnovationlabs.redinnovationnda.presentation.navigation.RedNdaApp
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()

        setContent {
            val container = remember { RedNdaContainer(applicationContext) }
            RedNdaTheme {
                RedNdaApp(container = container)
            }
        }
    }
}
