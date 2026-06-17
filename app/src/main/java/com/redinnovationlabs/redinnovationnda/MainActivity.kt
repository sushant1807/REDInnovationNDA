package com.redinnovationlabs.redinnovationnda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.redinnovationlabs.redinnovationnda.data.di.RedNdaContainer
import com.redinnovationlabs.redinnovationnda.presentation.navigation.RedNdaApp
import com.redinnovationlabs.redinnovationnda.presentation.theme.RedNdaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val container = remember { RedNdaContainer() }
            RedNdaTheme {
                RedNdaApp(container = container)
            }
        }
    }
}
