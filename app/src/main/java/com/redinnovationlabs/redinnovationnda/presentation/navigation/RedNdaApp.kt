package com.redinnovationlabs.redinnovationnda.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.redinnovationlabs.redinnovationnda.data.di.RedNdaContainer
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.RedNdaViewModelFactory

@Composable
fun RedNdaApp(container: RedNdaContainer) {
    val navController = rememberNavController()
    val factory = remember(container) { RedNdaViewModelFactory(container) }

    RedNdaNavHost(
        navController = navController,
        viewModelFactory = factory
    )
}
